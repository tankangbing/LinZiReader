package com.hn.epub;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import android.net.Uri;
import android.sax.Element;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;

/**   
* @Description: 用于解析epub文件，由于安全原因，要求epub内容不能解压到硬盘，必须在内存中操作
*/ 
public class Book {
	
	 // the container XML
    private static final String XML_NAMESPACE_CONTAINER = "urn:oasis:names:tc:opendocument:xmlns:container";
    private static final String XML_ELEMENT_CONTAINER = "container";
    private static final String XML_ELEMENT_ROOTFILES = "rootfiles";
    private static final String XML_ELEMENT_ROOTFILE = "rootfile";
    private static final String XML_ATTRIBUTE_FULLPATH = "full-path";
    private static final String XML_ATTRIBUTE_MEDIATYPE = "media-type";

    // the .opf XML
    private static final String XML_NAMESPACE_PACKAGE = "http://www.idpf.org/2007/opf";
    private static final String XML_ELEMENT_PACKAGE = "package";
    private static final String XML_ELEMENT_MANIFEST = "manifest";
    private static final String XML_ELEMENT_MANIFESTITEM = "item";
    private static final String XML_ELEMENT_SPINE = "spine";
    private static final String XML_ATTRIBUTE_TOC = "toc"; 
    private static final String XML_ELEMENT_ITEMREF = "itemref";
    private static final String XML_ATTRIBUTE_IDREF = "idref";    

    public String getOpfFileName() { return mOpfFileName; }
    public ArrayList<ManifestItem> getSpine() { return mSpine; }
    public Manifest getManifest() { return mManifest; }
    public TableOfContents getTableOfContents() { return mTableOfContents; }
    public int CurrentChapter;
	private ZipFile mZip;    
    private String mOpfFileName;
    private String mTocID;
    public ArrayList<ManifestItem> mSpine;   
    private Manifest mManifest;
    private TableOfContents mTableOfContents;
    
    public Book(String fileName) {
        mSpine = new ArrayList<ManifestItem>();
        mManifest = new Manifest();
        mTableOfContents = new TableOfContents();
        try {
            mZip = new ZipFile(fileName);
            parseEpub();
        } catch (IOException e) {
            Log.e("reader2014", "Error opening file", e);
        }
    }
    public ResourceResponse fetch(Uri resourceUri) {
        String resourceName = url2ResourceName(resourceUri);        
        ManifestItem item = mManifest.findByResourceName(resourceName);
        if (item != null) {
            ResourceResponse response = new ResourceResponse(item.getMediaType(), 
                    fetchFromZip(resourceName));
            response.setSize(mZip.getEntry(resourceName).getSize());
            return response;
        }        
        return null;
    }
    /**   
    * @Description: 返回epub压缩包的流，用于读取epub内容
    */
	public InputStream fetchFromZip(String fileName) {
        InputStream in = null;
        if(fileName.startsWith("http://localhost/"))
    		fileName = fileName.substring(17);       
        ZipEntry containerEntry = mZip.getEntry(fileName);
        if (containerEntry != null) {
            try {            	
                in = mZip.getInputStream(containerEntry);
            } catch (IOException e) {
                
            }
        }

        if (in == null) {            
        }        
        return in;
    }
	
   
	private void parseEpub() {
        // clear everything
        mOpfFileName = null;
        mTocID = null;
        mSpine.clear();
        mManifest.clear();
        mTableOfContents.clear();
        
        // get the "container" file, this tells us where the ".opf" file is
        parseXmlResource("META-INF/container.xml", constructContainerFileParser());

        if (mOpfFileName != null) {
            parseXmlResource(mOpfFileName, constructOpfFileParser());
        }

        if (mTocID != null) {
            ManifestItem tocManifestItem = mManifest.findById(mTocID);
            if (tocManifestItem != null) {
                String tocFileName = tocManifestItem.getHref();
                HrefResolver resolver = new HrefResolver(tocFileName);
                parseXmlResource(tocFileName, mTableOfContents.constructTocFileParser(resolver));
            }
        }
    }

    private void parseXmlResource(String fileName, ContentHandler handler) {
        InputStream in = fetchFromZip(fileName);
        if (in != null) {
            parseXmlResource(in, handler, null);
        }
    }
    public static void parseXmlResource(InputStream in, ContentHandler handler, XMLFilterImpl lastFilter) {
        if (in != null) {
            try {
                SAXParserFactory parseFactory = SAXParserFactory.newInstance();
                XMLReader reader = parseFactory.newSAXParser().getXMLReader();
                reader.setContentHandler(handler);

                try {
                    InputSource source = new InputSource(in);
                    source.setEncoding("UTF-8");
                    
                    if (lastFilter != null) {
                        // this is a chain of filters, setup the pipeline
                        ((XMLFilterImpl)handler).setParent(reader);
                        lastFilter.parse(source);
                    } else {
                        // simple content handler
                        reader.parse(source);
                    }
                } finally {
                    in.close();
                }
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                //Log.e(Globals.TAG, "Error reading XML file ", e);
            } catch (SAXException e) {
                //Log.e(Globals.TAG, "Error parsing XML file ", e);
            }
        }
    }
    public ContentHandler constructContainerFileParser() {
        // describe the relationship of the elements
        RootElement root = new RootElement(XML_NAMESPACE_CONTAINER, XML_ELEMENT_CONTAINER);
        Element rootfiles = root.getChild(XML_NAMESPACE_CONTAINER, XML_ELEMENT_ROOTFILES);
        Element rootfile = rootfiles.getChild(XML_NAMESPACE_CONTAINER, XML_ELEMENT_ROOTFILE);
        
        rootfile.setStartElementListener(new StartElementListener(){
            public void start(Attributes attributes) {
                String mediaType = attributes.getValue(XML_ATTRIBUTE_MEDIATYPE);
                if ((mediaType != null) && mediaType.equals("application/oebps-package+xml")) {
                    mOpfFileName = attributes.getValue(XML_ATTRIBUTE_FULLPATH); 
                }
            }
        });
        return root.getContentHandler();
    }
    
    /*
     * build parser to parse the ".opf" file,
     * @return parser
     */
    public ContentHandler constructOpfFileParser() {
        // describe the relationship of the elements
        RootElement root = new RootElement(XML_NAMESPACE_PACKAGE, XML_ELEMENT_PACKAGE);
        Element manifest = root.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_MANIFEST);
        Element manifestItem = manifest.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_MANIFESTITEM);
        Element spine = root.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_SPINE);
        Element itemref = spine.getChild(XML_NAMESPACE_PACKAGE, XML_ELEMENT_ITEMREF);

        final HrefResolver resolver = new HrefResolver(mOpfFileName);
        manifestItem.setStartElementListener(new StartElementListener(){
            public void start(Attributes attributes) {
                mManifest.add(new ManifestItem(attributes, resolver));
            }
        });
        
        // get name of Table of Contents file from the Spine
        spine.setStartElementListener(new StartElementListener(){
            public void start(Attributes attributes) {
                mTocID = attributes.getValue(XML_ATTRIBUTE_TOC); 
            }
        });
        
        itemref.setStartElementListener(new StartElementListener(){
            public void start(Attributes attributes) {
                String temp = attributes.getValue(XML_ATTRIBUTE_IDREF);
                if (temp != null) {
                    ManifestItem item = mManifest.findById(temp);
                    if (item != null) {
                        mSpine.add(item);
                    }
                }
            }
        });
        return root.getContentHandler();
    }
    private static String url2ResourceName(Uri url) {
        // we only care about the path part of the URL
        String resourceName = url.getPath();
        
        // if path has a '/' prepended, strip it
        if (resourceName.charAt(0) == '/') {
            resourceName = resourceName.substring(1);
        }
        return resourceName;
    }
	public static Uri resourceName2Url(String resourceName) {
        // build path assuming local file.
        // pack resourceName into path section of a file URI
        // need to leave '/' chars in path, so WebView is aware
        // of path to current resource, so it can can correctly resolve
        // path of any relative URLs in the current resource.
        return new Uri.Builder().scheme("http")
                .encodedAuthority("localhost:" + "1025")
                .appendEncodedPath(Uri.encode(resourceName, "/"))
                .build();
    }
}
