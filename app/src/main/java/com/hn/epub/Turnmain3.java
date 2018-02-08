package com.hn.epub;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hn.linzi.R;
import com.hn.linzi.activity.DownloadManageActivity;
import com.hn.linzi.utils.BookDBHelper;
import com.hn.linzi.utils.MyTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.TelephonyManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
* @Description: epub阅读主页面，使用viewpager类实现翻页效果，viewpager缓存3个页面，即当前页，前一页、后一页，加上epub本身的结构性，本类的翻页逻辑比较复杂
*/ 
public class Turnmain3 extends Activity implements OnPageChangeListener , MenuBottomActivity.OnMenuItemSelectedListener,
MenuTopActivity.OnMenuItemSelectedListener{

	static boolean firstLoad;//第一次加载openChapter用
	static boolean changeTextSize;//改变字体后需要获取layout
	static boolean changeLineSpace;//改变行间距后需要获取layout
	static boolean isRotate;//是否翻转
	
	static ReadView readview[];//epub正文view
	static int totalLen=0;//epub内容总长度
	//BufferedReader reader;
	//CharBuffer buffer = CharBuffer.allocate(4096);
	
	private ViewPager viewpager;
	static View list1[];//3个页面的activity
	static List<String> chapterName;//章节名称
	LEDView list_led[]=new LEDView[3];//ledview包含电池时间，进度2个view
    static TextView list_chapter[];//章节textview
    static TextView list_time[];//电池时间textview
    static TextView list_percent[];//进度textview
    static FrameLayout list_bookmarkOuter[];//包含书签
    static ImageView list_bookmark[];//书签
	DisplayMetrics dm;
	
	public String bookurl;//epub文件位置
	
	private int mWidth = 1080;//屏幕宽度
	private int mHeight = 1920;//屏幕高度
	
	public Book book;//epub
	static int chapter_len;//章节长度
	
	static int bkcolor,textcolor;//图书背景颜色、文本颜色
	static int fontsize;//正文文字大小
	static float linespace;//正文行间距
	static float bright;//阅读亮度
	static float sysBright;//系统原有亮度
	static int waketime;//熄屏时间
	
	MenuTopActivity mMenuTop=null;//顶部菜单
	MenuBottomActivity mMenuBottom=null;//底部菜单
	static String bookmarkstr;//书签文本				
	Mypageradapter myadapter;//viewpager的adapter
	
    static String daymode;//日间、夜间模式
    static String bookid;//图书id号
    
	static int curChapter=0;//当前章节
	//public CharBuffer buffers[];
	static SpannableStringBuilder ssb1[];//章节内容
	Pattern p=Pattern.compile("bmp:(.*)\r\n");//处理epub中的图片
	
    static PowerManager.WakeLock wakeLock;//用于控制熄屏
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.turnmain3);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
			}

		
		firstLoad=true;
		isRotate=false;
		changeTextSize=true;
		changeLineSpace=false;	   
		
		curChapter=0;
	    beginPos=0;
	    
	    endPos=0;
	    preView=3000;//上一个viewpager号码
		vpPage=3000;
		fontsize=20;
		linespace=1.3f;
		
		sysBright=super.getWindow().getAttributes().screenBrightness;		
		waketime=2;
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); 	    
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "TAG");  
		
	    readview=new ReadView[3];//正文
		list1=new View[3];//turnpage页面
		chapterName=new ArrayList<String>();//章节名称
		list_led=new LEDView[3];//ledview包含电池时间，进度2个view
	    list_chapter=new TextView[3];//章节textview
	    list_time=new TextView[3];//电池时间textview
	    list_percent=new TextView[3];//进度textview
	    list_bookmarkOuter=new FrameLayout[3];
	    list_bookmark=new ImageView[3];

		SharedPreferences sharedPreferences = getSharedPreferences("reader", Activity.MODE_PRIVATE); 
		bookmarkstr = sharedPreferences.getString("bookmark1", "");
		viewpager = (ViewPager) findViewById(R.id.viewpager);
				
		initlist();
		myadapter=new Mypageradapter();
		viewpager.setAdapter(myadapter);
		viewpager.setCurrentItem(3000,false);
		viewpager.setOnPageChangeListener(this);
			
		dm =getResources().getDisplayMetrics();  
		mWidth=dm.widthPixels;
		mHeight=dm.heightPixels;
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);				
		
		try {
			Intent intent =getIntent();
			bookurl=intent.getStringExtra("bookurl");
			
			if(bookurl==null||bookurl=="")
				bookurl="download/26.epub";
			String SDCard = "";
			if(MyTools.getExtSDCardPaths().size() > 0){
				SDCard = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/";
			}else {
				SDCard = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/";
			}
//			File DL = getExternalFilesDir("BOOKS");
//			String epub=DL.getPath() + "/"+bookurl;	
			String epub=bookurl;
			File f=new File(epub);
			if(!f.exists())
			{
				readview[0].setText("epub文件不存在，请重新下载");
				readview[1].setText("epub文件不存在，请重新下载");
				readview[2].setText("epub文件不存在，请重新下载");
			}
			else
			{
				bookid=f.getName();
				System.out.println("filename====" + f.getName());
				openbook(bookurl);		
				
				viewpager.setPageMargin(20);
				
				String[] bookstarts=ReadData("bookstart").split("\\|");
			    for (int i = 0;i<bookstarts.length; i++) 
			    {	         
			    	 String[] bookstart=bookstarts[i].split(" ");	    	 
			    	 String bookname=bookstart[0];
			    	 if(bookname.equals(bookid))
			    	 {
			    		 curChapter =Integer.parseInt(bookstart[1]);
			    		 beginPos=Integer.parseInt(bookstart[2]);	    	
			    		 break;
			    	 }
			    }
			    String bookBright=ReadData("bookBright");
			    if(!bookBright.equals(""))
			    {
			    	bright=Float.parseFloat(bookBright);
			    	setScreenBrightness(bright);
			    }
			    else
			    	bright=super.getWindow().getAttributes().screenBrightness;
			    
			    
			  
				if(savedInstanceState != null) {
			        curChapter=savedInstanceState.getInt("rotateChapter");
			        beginPos=savedInstanceState.getInt("rotateBeginPos");
			        //endPos=savedInstanceState.getInt("rotateEndPos");
			        //turnDirect=savedInstanceState.getString("turnDirect");
			        isRotate=true;
			    }		
				
				mMenuTop = new MenuTopActivity(this, (com.hn.epub.MenuTopActivity.OnMenuItemSelectedListener)this,getLayoutInflater());
				mMenuBottom = new MenuBottomActivity(this, (com.hn.epub.MenuBottomActivity.OnMenuItemSelectedListener)this, getLayoutInflater());
				
				try {
		        	getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
		        }catch (NoSuchFieldException e) {	        	
		        }catch (IllegalAccessException e) {
		        	Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);
		        }
				
				//注册广播接受者java代码
				IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
				//创建广播接受者对象
				BatteryReceiver batteryReceiver = new BatteryReceiver();				
				//注册receiver
				registerReceiver(batteryReceiver, intentFilter);
			}
			
		} catch (IOException e1) {			
			e1.printStackTrace();
			Toast.makeText(this, "电子书不存在",Toast.LENGTH_SHORT).show();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);	    
	    outState.putInt("rotateChapter", curChapter);
	    outState.putInt("rotateBeginPos", beginPos);
	}
	@Override
	protected void onResume() {
		super.onResume();
		for(int i=0;i<3;i++)
			list_led[i].start();
		
		if(wakeLock.isHeld())
      	  wakeLock.release();		
		wakeLock.acquire(Turnmain3.waketime*60*1000);  		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		for(int i=0;i<3;i++)
			list_led[i].stop();
	}
	/**   
	* @Description: 初始化页面
	*/ 
	private void initlist() {
		String lineSpace=ReadData("linespace");
	    if(!lineSpace.equals(""))
	    {
	    	linespace=Float.parseFloat(lineSpace);
	    }
	    String wakeTime=ReadData("waketime");
	    if(!wakeTime.equals(""))
	    {
	    	waketime=Integer.parseInt(wakeTime);
	    }
	    
		LayoutInflater inflater =getLayoutInflater();
		list1[0] = inflater.inflate(R.layout.turnpage1, null);
		list1[1] = inflater.inflate(R.layout.turnpage2, null);
		list1[2] = inflater.inflate(R.layout.turnpage3, null);
		
		bkcolor=Color.WHITE;
		textcolor=Color.BLACK;
		for(int i=0;i<3;i++)
		{
			list_led[i]=(LEDView)list1[i].findViewById(R.id.ledview);			
			list_chapter[i]=(TextView)list1[i].findViewById(R.id.tvtop);
			list_bookmarkOuter[i]=(FrameLayout)list1[i].findViewById(R.id.bookmarkOuter);
			list_bookmark[i]=(ImageView)list1[i].findViewById(R.id.bookmark);
			readview[i]=(ReadView)list1[i].findViewById(R.id.read_view);
			readview[i].setTag("readview"+i);
			list_time[i]=(TextView)list_led[i].findViewById(R.id.ledview_clock_time);
			list_percent[i]=(TextView)list_led[i].findViewById(R.id.ledview_percent);	
					
			final int i2=i;
			list_bookmarkOuter[i].setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					ImageView iv=(ImageView)v.findViewById(R.id.bookmark);
					if(iv.getVisibility()==View.VISIBLE)
					{
						iv.setVisibility(View.INVISIBLE);
						removeBookmark();
					}
					else
					{
						iv.setVisibility(View.VISIBLE);
						addBookmark();
					}
				}
			});
			readview[i].setOnTouchListener(new OnTouchListener() {			
				@Override
				public boolean onTouch(View v, MotionEvent e) {				
					if (v == readview[i2] ) {					
							if (e.getAction() == MotionEvent.ACTION_UP) {
								if(e.getX()>(mWidth/3)&&e.getX()<(mWidth*2/3)&&e.getY()>(mHeight/3)&&e.getY()<(mHeight*2/3))
								{							
									doMenu();
								}
								if(e.getY()>(mHeight*3/4)||(e.getX()>(mWidth*2/3)&&e.getY()>(mHeight/4)))
								{			
									if(readview[(vpPage+1)%3].getText().length()>0)
										viewpager.setCurrentItem(vpPage+1);
								}
								if(e.getX()<(mWidth/3)&&e.getY()>(mHeight/4)&&e.getY()<(mHeight*3/4))
								{					
									if(readview[(vpPage-1)%3].getText().length()>0)
										viewpager.setCurrentItem(vpPage-1);
								}
							}
					}
					return true;
				}
			});	
			list_chapter[i].setPadding(20, 0, 0, 0);			
			readview[i].setLineSpacing(0.0f, linespace);
			readview[i].setPadding(30, 20, 30, 10);	
			String fontsizeStr=ReadData("fontSize");
			if(!fontsizeStr.equals(""))
				fontsize=Integer.parseInt(fontsizeStr);
			readview[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontsize);
		}
		
		daymode=ReadData("daymode");
		String bkc=ReadData("bkcolor");
		if(bkc.equals(""))
			bkc=Color.WHITE+"";
		bkcolor=Integer.parseInt(bkc);
		if(daymode.equals("night"))
			setcolor(Color.BLACK,Color.GRAY,true);
		else
		{
			setcolor(bkcolor,textcolor,false);
		}
	}
	/**   
	* @Description: 设置颜色
	*/ 
	public void setcolor(int bkcolor1,int textcolor1,boolean isSetDayNight)
	{
		if(!isSetDayNight)
		{
			bkcolor=bkcolor1;
			textcolor=textcolor1;
		}
		for(int i=0;i<3;i++)
		{	
			list_chapter[i].setBackgroundColor(bkcolor1);
			list_chapter[i].setTextColor(Color.GRAY);			
			
			list_time[i].setBackgroundColor(bkcolor1);
			list_time[i].setTextColor(Color.GRAY);
			list_percent[i].setBackgroundColor(bkcolor1);
			list_percent[i].setTextColor(Color.GRAY);
			
			readview[i].setBackgroundColor(bkcolor1);
			readview[i].setTextColor(textcolor1);			
		}		
	}

	/**   
	* @Description: 打开epub文件
	*/ 
	public void openbook(String strFilePath) throws Exception {
		String epub=/*Environment.getExternalStorageDirectory().getPath() + "/"+*/strFilePath;	
		//epub="file:///android_asset/"+strFilePath;
		
		book=new Book(epub);
		chapter_len=book.getTableOfContents().size();

		ssb1 =new SpannableStringBuilder[chapter_len];
        
		String deskey="";//des密码
		String imei=((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId(); 
		InputStream inLic=book.fetchFromZip("lic.dat");			
		if(inLic!=null)//是否加密epub
		{
			System.out.println("加密版文件");
			
			
			/*解密步骤
			1、下载epub，此步骤一般在书架阶段完成
			试读epub使用http://data.iego.net:85/book/epub.aspx
			加密epub使用http://data.iego.net:85/book/epubx.aspx
			下载时提供参数图书id，imei
			比如：http://data.iego.net:85/book/epubx.aspx?lib=classic&id=26&code=88888888&n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&imei=000000000000000
			获得epub图书26.epub
			
			2、打开26.epub
			判断加密epub
			epub包中含有证书文件lic.dat
			
			从认证服务器获取RSA公用密钥
			提供参数图书id，imei
			例如：http://data.iego.net:85/book/getrsakey.aspx?lib=classic&id=26&n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&imei=000000000000000
			
			3、使用RSA解密lic.dat文件
			
			解密后的lic.dat		
			ea^3C)RTQmsd9nW8*G>|7I6E 2015/9/19/13/52/31 43200 000000000000000
			字段以空格分隔，字段含义如下
			des密码 开始日期时间 允许使用时间（分钟） imei
			
			4、检查imei是否合法
			
			5、检查时间是否过期
			
			6、使用des密码解密epub内容
			*/
			String bookid=epub.substring(epub.lastIndexOf("_")+1).replace(".epub", "");//获取bookid
			System.out.println("bookid=====" + bookid);
			SharedPreferences sp = getSharedPreferences("SP",
					MODE_PRIVATE);
			String keyurl="http://data.iego.net:85/book/getrsakey.aspx?lib=classic&id="+bookid+"&n=" + sp.getString("UserName", "") +
					"&d=" +sp.getString("KEY","" ) + "&imei=" + imei;
			HttpGet httpGet = new HttpGet(keyurl);
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpResponse response = httpClient.execute(httpGet);
	        String strprivatekey= showResponseResult(response);
	        PrivateKey privateKey = RsaHelper.decodePrivateKeyFromXml(strprivatekey);//获取rsa私有密钥
	         
			StringBuilder stringBuilder = new StringBuilder();
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inLic));
	        boolean firstLine = true;
	        String line = null; ;
	        while((line = bufferedReader.readLine()) != null){
	            if(!firstLine){
	                stringBuilder.append(System.getProperty("line.separator"));
	            }else{
	                firstLine = false;
	            }
	            stringBuilder.append(line);
	        }	     
	        
	        String licstr = "";
	        try
	        {
	           licstr=new String(RsaHelper.decryptData(Base64Helper.decode(stringBuilder.toString()), privateKey), "UTF-8");//解密lic.dat
	           System.out.println(stringBuilder.toString());
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        
			if(!licstr.equals(""))
			{
		        String[] lics=licstr.split(" ");
		        deskey=lics[0];	        
		        String imei2=lics[3];
		        if(!imei.equals(imei2))//检查imei
		        {
		        	Toast.makeText(this, "无效证书",Toast.LENGTH_SHORT).show();
		        	deskey="";	        
		        }
		        String startDT=lics[1];
		        String howlong=lics[2];
		        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/H/m/s");
		        Date startdt=format.parse(startDT);
		        Calendar calendar=Calendar.getInstance();   
		        calendar.setTime(startdt);
		        calendar.add(Calendar.MINUTE, Integer.parseInt(howlong));
		        
		        Calendar calnow=Calendar.getInstance();   
		        calnow.setTime(new Date());	    
		        System.out.println("calendar.before(calnow)=" + calendar.before(calnow));
		        if(calendar.before(calnow))//检查时间是否过期
		        {
		        	Toast.makeText(this, "证书已过期",Toast.LENGTH_SHORT).show();
		        	deskey="";
		        	downloadBook("chongxin");
		        }
			}
			else
				Toast.makeText(this, "无效证书",Toast.LENGTH_SHORT).show();
		}
		totalLen=0;//文章总长度
		for(int i=0;i<chapter_len;i++)
		{
			String filename=book.getSpine().get(i).getHref();
			String path=filename.substring(0, filename.indexOf("/", 0));
			InputStream in=book.fetchFromZip(filename);				
	        
	        ByteArrayOutputStream out=new ByteArrayOutputStream();
	        byte[] buffer=new byte[1024*4];
	        int n=0;
	        while ( (n=in.read(buffer)) !=-1) {
	            out.write(buffer,0,n);
	        }
	        
	        String tmp= "";
	        if(deskey.length()==24)//使用des密码解密
	        {
	        	byte[] key=deskey.getBytes();
	        	byte[] str1 = Des3.des3DecodeECB(key, out.toByteArray());
	        	tmp=new String(str1, "UTF-8");	       
	        }
	        else
	        	tmp=new String(out.toByteArray(), "UTF-8");	
	        
	        tmp=tmp.replaceAll("\\<.*?>","");	//消除html标记
			tmp=tmp.trim();
			tmp=tmp.replaceAll("，", ", ");
			tmp=tmp.replaceAll("“", "\" ");   
		    tmp=tmp.replaceAll("”", "\" "); 
		    tmp=tmp.replaceAll("。", ". ");  
		    tmp=tmp.replaceAll("：", ": ");
		    tmp=tmp.replaceAll("、", "、 ");
		    tmp=tmp.replaceAll("！", "! ");		   	
		   
			ssb1[i]=new SpannableStringBuilder(tmp);	

			Matcher m=p.matcher(ssb1[i]);
			while(m.find()&&i!=5)
			{
				MatchResult mr=m.toMatchResult();
				
				String bmpPath=path+"/"+mr.group(1);
					
				InputStream is=book.fetchFromZip(bmpPath);				
	       		Bitmap bmp=BitmapFactory.decodeStream(is);
	       		
	       		int w=bmp.getWidth();
	       		int h=bmp.getHeight();
	       		
				Drawable drawable =new BitmapDrawable(bmp);
				drawable.setBounds(0, 0, 300*w/h,300);//lheight*5*w/h, lheight*5);//这里设置图片的大小    
				ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);     
				
				ssb1[i].setSpan(imageSpan, mr.start(), mr.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);				
				     
			}

			totalLen+=ssb1[i].length();
		}
		chapterName.clear();
		TableOfContents toc = book.getTableOfContents();			
		for(int i=0;i<toc.size();i++)
		{				
			chapterName.add(toc.get(i).getNavLabel());						
		}		
	}
	
	/**   
	* @Description: 刷新curView页面内容
	*/ 
	private int tryLen=2048;
	static void loadPage(int curView) {	
		int chapter=readview[curView].chapter;
		int start=readview[curView].beginPos;
		int end=readview[curView].endPos;
		if(end==0)
			end=start+2048;
		if(end>ssb1[chapter].length())
		{
			end=ssb1[chapter].length();			
		}		
		if(end>start)
		{
			readview[curView].setText(ssb1[chapter].subSequence(start, end));
			readview[curView].endPos =start+readview[curView].getCharNum();
		}
		else
		{
			readview[curView].setText(ssb1[chapter]);
			readview[curView].endPos=end;
		}
		
		int bklen=0;
    	for(int j=0;j<chapter;j++)
    		bklen+=ssb1[j].length();
    	
		float per=(bklen+beginPos)*100.0f/totalLen;
		if(per==0)
			list_percent[curView].setText("0.0%");
		else
		{
			if(per>100)per=100;
			java.text.DecimalFormat df=new java.text.DecimalFormat("#.#");		
			list_percent[curView].setText(df.format(per)+"%");
		}			
		list_chapter[curView].setText(chapterName.get(chapter));	

		list_bookmark[curView].setVisibility(View.INVISIBLE);
		String[] data=bookmarkstr.split("\\|");
	    for (int i = data.length-1; i>=0; i--) 
	    {           
	         if(data[i].length()==0)
	            continue;
	         
	    	 String[] listBookmark=data[i].split(" ");
	    	 if(listBookmark.length!=4)
	    		 continue;
	    	 String bookname=listBookmark[0];
	    	 int chapterNum =Integer.parseInt(listBookmark[1]);
	    	 int len=Integer.parseInt(listBookmark[2]);
	    	    	
	    	 String text=readview[curView].getText().toString();
	    	 String text2=(Turnmain3.ssb1[chapterNum].subSequence(len, Turnmain3.ssb1[chapterNum].length())).toString();
	    	 if(bookname.equals(bookid)	&& text2.indexOf(text)!=-1&&text2.indexOf(text)<100)
	    	 {		 
	    		 list_bookmark[curView].setVisibility(View.VISIBLE);
	    		 break;
	    	 }
	    }
	}
	/**   
	* @Description: 用于实现viewpager的adapter
	*/ 
	class Mypageradapter extends PagerAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6000;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub			
			return arg0 == arg1;
		}
		@SuppressLint("WrongCall")
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int pos= position%3;
			if(((ViewPager)container).getChildCount()==3)  
			{  
				((ViewPager)container).removeView(list1[pos]);  
			}		
			container.addView(list1[pos],0);				
			return list1[pos];
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	static int beginPos=0;
	static int endPos=0;
	static int preView=3000;//上一个viewpager号码
	static int vpPage=3000;
	
	/**   
	* @Description: viewpager翻页时触发，arg0代表当前页码
	*/ 
	@Override
	public void onPageSelected(int arg0) 
	{
		if(isRotate)
			vpPage=arg0;
		if(preView==arg0||isRotate)//屏幕翻转时不进行任何处理
			return;
		
		boolean toBegin=false;//判断是否到达最前页
		boolean toEnd=false;//判断是否到达最后一页

		if(arg0<preView)
		{			
			CharSequence str1=ssb1[0].subSequence(0, 50);
			CharSequence str2=readview[(arg0+1)%3].getText();
			int len=50;
			if(str2.length()<50)
				len=str2.length();
			str2=str2.subSequence(0, len);
			if(str1.toString().equals(str2.toString()))
					toBegin=true;
		}
		else
		{
			int start1=ssb1[chapter_len-1].length()-50;
			if(start1<0)start1=0;
			
			CharSequence str3=ssb1[chapter_len-1].subSequence(start1, ssb1[chapter_len-1].length());
			CharSequence str4=readview[(arg0-1)%3].getText();
			int len2=str4.length()-50;
			if(len2<0)
				len2=0;
			str4=str4.subSequence(len2, str4.length());
			if("超出试读范围".equals(readview[(arg0)%3].getText().toString())){
				System.out.println("到达最后一页");
				downloadBook("wanquan");
			}
			if(str3.toString().equals(str4.toString())){
				toEnd=true;
			}
		}
		if(toBegin)
		{
			viewpager.setCurrentItem(arg0+1);
		}
		else if(toEnd)
		{
			viewpager.setCurrentItem(arg0-1);
		}
		else
		{
			vpPage=arg0;
			loadPages(arg0);
			
			preView=arg0;
		}
	}
	
	/**
	 * 重新下载epub图书
	 * flag=wanquan  下载完整加密版
	 * flag=chongxin 证书过期重新下
	 */
	private void downloadBook(final String flag) {
		// TODO Auto-generated method stub
		final SharedPreferences sp = getSharedPreferences("SP",MODE_PRIVATE);
		Dialog dialog = new AlertDialog.Builder(Turnmain3.this)
        .setTitle("是否下载完整版图书")
        .setMessage("是否下载完整版图书")
        .setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	Toast.makeText(getApplicationContext(), "下载完整版", Toast.LENGTH_SHORT).show();
            	String path = "";
    			if(MyTools.getExtSDCardPaths().size() > 0){
    				path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/" + bookid;
    			}else {
    				path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/" + bookid;
    			}
            	BookDBHelper bookDBHelper = new BookDBHelper(getApplicationContext());
            	Cursor cursor = bookDBHelper.select(null, "book_path=? and username=?", new String[]{path, sp.getString("UserName", "")});
            	cursor.moveToNext();
            	String url = cursor.getString(cursor.getColumnIndex("book_url"));
            	if(flag.equals("wanquan")){
            		String[] breakurl = url.split("epub.");
                	String newurl = breakurl[0] + "epubx." + breakurl[1];
                	System.out.println("newurl====" + newurl);
                	bookDBHelper.update(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
            				cursor.getString(4), cursor.getString(5), cursor.getString(6), newurl, path,
            				"false", "false", sp.getString("UserName", ""));
            	}else{
            		bookDBHelper.update(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
            				cursor.getString(4), cursor.getString(5), cursor.getString(6), url, path,
            				"false", "false", sp.getString("UserName", ""));
            	}
            	//删除图书
            	File file = new File(path); 
            	if (file.exists()) { // 判断文件是否存在
            		if (file.isFile()) { // 判断是否是文件
            			file.delete(); // delete()方法 你应该知道 是删除的意思;
            		} 
            	}
            	Bundle bundle = new Bundle();
            	bundle.putString("oldurl", url);
            	Intent intent = new Intent();
            	intent.putExtras(bundle);
            	intent.setClass(getApplicationContext(), DownloadManageActivity.class);
            	startActivity(intent);
            	finish();
            }
        })//设置确定按钮
        .setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	Toast.makeText(getApplicationContext(), "暂不下载", Toast.LENGTH_SHORT).show();
            }
        })//设置取消按钮
        .create();
		dialog.show();
	
	}
	
	/**   
	* @Description: 阅读翻页，只刷新一个页面，前进时刷新后一页，后退时刷新前一页
	*/ 
	static void loadPages(int arg0)
	{
		curChapter=readview[arg0%3].chapter;
		beginPos=readview[arg0%3].beginPos;
		if(arg0>preView)//方向->
		{	
			if(readview[arg0%3].endPos>=ssb1[curChapter].length())//进入下一章
			{				
				if(curChapter+1>=chapter_len)//到达尾部
				{
					readview[(arg0+1)%3].setText("");
					list_chapter[(arg0+1)%3].setText("");					
					list_bookmark[(arg0+1)%3].setVisibility(View.INVISIBLE);				
					list_time[(arg0+1)%3].setText("");
					list_percent[(arg0+1)%3].setText("");					
					return;
				}
				readview[(arg0+1)%3].chapter=curChapter+1;
				readview[(arg0+1)%3].beginPos=0;
				readview[(arg0+1)%3].endPos=0;
				loadPage((arg0+1)%3);				
			}			
			else
			{
				readview[(arg0+1)%3].chapter=curChapter;
				readview[(arg0+1)%3].beginPos=readview[arg0%3].endPos;
				readview[(arg0+1)%3].endPos=0;
				loadPage((arg0+1)%3);
			}
		}
		else
		{
			
			if(readview[arg0%3].chapter<=0&&readview[arg0%3].beginPos<=0)//到达最前页
			{			
				readview[(arg0-1)%3].setText("");
				list_chapter[(arg0-1)%3].setText("");					
				list_bookmark[(arg0-1)%3].setVisibility(View.INVISIBLE);					
				list_time[(arg0-1)%3].setText("");
				list_percent[(arg0-1)%3].setText("");
				return;
			}
			
			if(readview[arg0%3].beginPos<=0)//当前页在新章时后退一章
			{						
				readview[(arg0-1)%3].chapter=curChapter-1;									
				readview[(arg0-1)%3].endPos=ssb1[curChapter-1].length();		
				int len=tryLen((arg0-1)%3);
				readview[(arg0-1)%3].beginPos=ssb1[curChapter-1].length()-len;
				loadPage((arg0-1)%3);
			}	
			else
			{
				readview[(arg0-1)%3].chapter=curChapter;									
				readview[(arg0-1)%3].endPos=readview[arg0%3].beginPos;		
				int len=tryLen((arg0-1)%3);
				readview[(arg0-1)%3].beginPos=readview[(arg0-1)%3].endPos-len;
				loadPage((arg0-1)%3);
			}
			
		}
		preView=arg0;
	}
	/**   
	* @Description: 用于探测前一页面的开始点
	*/ 
	static int tryLen(int p)
	{
		int tmpPos=readview[p].endPos-2048;
		if(tmpPos<0)
			tmpPos=0;
		
		int end1=readview[p].endPos;
		if(end1>ssb1[readview[p].chapter].length())
			end1=ssb1[readview[p].chapter].length();
		CharSequence sq=ssb1[readview[p].chapter].subSequence(tmpPos, end1);
		String sq2=sq.toString();
		if(sq2.endsWith("\r\n"))//最后一行回车时会多一行，要去除
			sq2=sq2.substring(0,sq2.length()-2);
		
		readview[p].setText(sq2);
		
		int pagelines=readview[p].getLineNum();//每页多少行
		int tmpLines=readview[p].getLineCount();//当前多少行
		if(tmpLines>pagelines+1)
		{
			int tmpPos2=readview[p].getLayout().getLineEnd(tmpLines-pagelines-2);
			int tmpPos3=sq.length()-tmpPos2;
			return tmpPos3;
		}
		else
			return sq.length();			
	}
	
	/**   
	* @Description: 打开章节，刷新viewpager的全部三个页面
	*/ 
	static void openchapter(int chapter,int pos)
	{
		curChapter=chapter;
		beginPos=pos;
		if(pos==0)
		{
			if(chapter==0)//第1章位置0，前一页无内容
			{
				readview[(vpPage-1)%3].setText("");
				list_chapter[(vpPage-1)%3].setText("");					
				list_bookmark[(vpPage-1)%3].setVisibility(View.INVISIBLE);					
				list_time[(vpPage-1)%3].setText("");
				list_percent[(vpPage-1)%3].setText("");
			}
			else//大于二章位置为0，前一页要打开前一章尾部
			{				
				readview[(vpPage-1)%3].chapter=curChapter-1;									
				readview[(vpPage-1)%3].endPos=ssb1[curChapter-1].length();		
				int len=tryLen((vpPage-1)%3);
				readview[(vpPage-1)%3].beginPos=ssb1[curChapter-1].length()-len;
				loadPage((vpPage-1)%3);
			}
		}
		else
		{
			readview[(vpPage-1)%3].chapter=curChapter;									
			readview[(vpPage-1)%3].endPos=pos;		
			int len=tryLen((vpPage-1)%3);
			readview[(vpPage-1)%3].beginPos=pos-len;			
			loadPage((vpPage-1)%3);			
		}
		
		readview[vpPage%3].chapter=curChapter;				
		readview[vpPage%3].beginPos=pos;	
		readview[vpPage%3].endPos=0;
		loadPage(vpPage%3);		
		
		if(endPos>=ssb1[curChapter].length())
		{
			curChapter+=1;
			endPos=0;
		}		
		readview[(vpPage+1)%3].chapter=curChapter;				
		readview[(vpPage+1)%3].beginPos=readview[vpPage%3].endPos;	
		readview[(vpPage+1)%3].endPos=0;
		loadPage((vpPage+1)%3);		

	}

	/**   
	* @Description: 菜单事件处理
	*/ 
	@SuppressLint("WrongCall")
	@Override
	public void MenuItemSelectedEvent(int selection,int var2,int var3) {
		//int pos=curPos%3;
		if(selection==1)//点击返回按钮
		{				
			myExit();
			System.exit(0);
			//this.finish();			
		}
		if(selection == 4)//点击书签按钮
		{
			addBookmark();			
			ImageView iv=(ImageView)list_bookmark[vpPage%3].findViewById(R.id.bookmark);
			iv.setVisibility(View.VISIBLE);
			doMenu();
		}
		if(selection==7)//改变字体大小
		{			
			//float curTextsize=readview[0].getTextSize();
			if((var2==1&&fontsize>=12)||(var2==2&&fontsize<=36))
			{
				if(var2==1)
				{
					fontsize-=fontsize*2/10;				
				}
				if(var2==2)
				{
					fontsize+=fontsize*2/10;						
				}			
				
				changeTextSize=false;
				for(int i=0;i<3;i++)
				{
					//readview[i].setText("");
					readview[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontsize);
				}
				WriteData("fontSize",""+fontsize);
			}
		}
		
		if(selection==3)//目录
		{			
			Intent intent = new Intent(this, ChapterList.class);		
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);			
			startActivity(intent);							
			doMenu();									
			 
		} 
		if(selection==5)//翻转
		{	
			int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
			if(rotation==0)
				 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);                 
			else
                 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);		
		}
		if(selection==6)//进度条
		{
			Intent intent = new Intent(this, SBjump.class);		
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);			
			startActivity(intent);							
			doMenu();	
		}
		if(selection==8)//设置
		{
			Intent intent = new Intent(this, Setup.class);		
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);			
			startActivity(intent);							
			doMenu();	
		}
		if(selection==9)//日间/夜间
		{	
			daymode=ReadData("daymode");			
			if(daymode.equals("day"))
			{	
				daymode="night";
				WriteData("daymode","night");		
				setcolor(Color.BLACK,Color.GRAY,true);				
			}
			else
			{
				daymode="day";
				WriteData("daymode","day");
				setcolor(bkcolor,textcolor,true);				
			}				

		}
		
		if(selection==31)//亮度
		{
			bright=var2/100f;
			setScreenBrightness(var2/100f);
			WriteData("bookBright",bright+"");
		}
		if(selection==32)//系统亮度
		{
			if(var2==1)
			{
				setScreenBrightness(sysBright);	
			}
			else
				setScreenBrightness(bright);	
		}
		
		if(selection==33)//改变背景颜色
		{			
			setcolor(var2,textcolor,false);
			daymode="day";
			WriteData("daymode","day");
			WriteData("bkcolor",var2+"");	
			WriteData("bkcolor2",var3+"");
		}
		
	}	
	/**   
	* @Description: 按键处理
	*/ 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) {    //返回键	
	    	myExit();	    		    		
	    	//System.exit(0);	    	

	    } else if(keyCode == KeyEvent.KEYCODE_MENU) {//菜单键
	   		doMenu();	       
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	private void doMenu() {
		if (mMenuTop.isShowing()) {
			mMenuTop.hide();
			mMenuBottom.hide();
		} else {			
			mMenuTop.show(viewpager);
			mMenuBottom.show(viewpager);
		}
	}
	/**   
	* @Description: 记录设置数据
	*/ 
	public void WriteData(String name,String value) 
	{ 
		SharedPreferences mySharedPreferences = getSharedPreferences("reader", Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		editor.putString(name, value);		
		//editor.putString(name, "");
		editor.commit();		
	} 
	/**   
	* @Description: 读取设置数据
	*/ 
	String ReadData(String name) 
	{ 
		SharedPreferences sharedPreferences = getSharedPreferences("reader", Activity.MODE_PRIVATE); 
		String value = sharedPreferences.getString(name, "");
		return value;
	} 
	/**   
	* @Description: 设置亮度
	*/ 
	public void setScreenBrightness(float num){
		WindowManager.LayoutParams layoutParams=super.getWindow().getAttributes();
		layoutParams.screenBrightness=num;//设置屏幕的亮度
		super.getWindow().setAttributes(layoutParams);
	}
	/**   
	* @Description: 添加书签
	*/ 
	public void addBookmark()
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		int wz=beginPos;//-readview[vpPage%3].getCharNum();
		String str1=bookid+" "+curChapter + " " + wz + " "+str;
		String bookmark=ReadData("bookmark1");
		if(bookmark.indexOf(str1)!=-1)
			bookmark=bookmark.replaceAll("\\|"+str1, "");
		WriteData("bookmark1",bookmark+"|"+str1);
		bookmarkstr=bookmark+"|"+str1;
		
	}
	/**   
	* @Description: 删除书签
	*/ 
	public void removeBookmark()
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		int wz=beginPos;//-readview[vpPage%3].getCharNum();
		String str0="\\|"+bookid+" " + curChapter + " " + wz+"[^\\|]*";
		
		bookmarkstr=bookmarkstr.replaceAll(str0, "");
		WriteData("bookmark1",bookmarkstr);
	}
	/**   
	* @Description: 退出是保存数据
	*/ 
	public void myExit()
	{
		String str1=bookid+" "+curChapter + " " + beginPos;
		String bookstart=ReadData("bookstart");

		String str0="\\|"+bookid+" "+"[^\\|]*";		
		bookstart=bookstart.replaceAll(str0, "");		
		WriteData("bookstart",bookstart+"|"+str1);		
		
		WriteData("bookBright",bright+"");
		WriteData("linespace",linespace+"");
		WriteData("waketime",waketime+"");
		
		if(wakeLock.isHeld())
	      	  wakeLock.release();	
	}
	/**
     * 获取http响应结果
     */
    private String showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return "";
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;
            }

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

	/**
	 * 广播接受者
	 */
	static int lvl=100;//电量
	class BatteryReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//判断它是否是为电量变化的Broadcast Action
			if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
				//获取当前电量
				int level = intent.getIntExtra("level", 0);
				//电量的总刻度
				int scale = intent.getIntExtra("scale", 100);
				//把它转成百分比
				//tv.setText("电池电量为"+((level*100)/scale)+"%");
				lvl=level;
			}
		}
		
	}
	
	


}
