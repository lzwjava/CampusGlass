package lzw.campus.glass;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Login{
	static void print(String format, Object... args) {
		System.out.println(String.format(format, args));
	}

	static int checkLogin(String username,String password) {
		if(!isConnectUrl("http://www.baidu.com")){
			return Constant.notConnectInet;
		}
		if(!isConnectUrl("http://login.bjfu.edu.cn/index.jsp")){
			return Constant.notConnectSchool;
		}
		HttpClient httpClient = new DefaultHttpClient();
		String ip;
		int resultCode=0;
		try{
		  ip=cssQueryFirstText("http://login.bjfu.edu.cn/index.jsp","span.login_txt");
		  Util.log("%s",ip);
		  resultCode=doPost(httpClient,"http://login.bjfu.edu.cn/checkLogin.jsp",
				"username",username,"password",password,
				"ip",ip,"action","checkLogin.jsp");
		}catch(Exception e){
			return Constant.InetException;
		}
		if(resultCode==302){
			return Constant.loginSuccess;
		}else{
			return Constant.wrongPassword;
		}
	}
	
	static void login(String username,String password) 
	 throws Exception{
		HttpClient httpClient = new DefaultHttpClient();
		String ip;
		String userId;
		ip=cssQueryFirstText("http://login.bjfu.edu.cn/index.jsp","span.login_txt");
		doPost(httpClient,"http://login.bjfu.edu.cn/checkLogin.jsp",
				"username",username,"password",password,
				"ip",ip,"action","checkLogin.jsp");
		String content=doGet(httpClient,"http://login.bjfu.edu.cn/user/index.jsp",
		  "ip",ip,"action","connect");
		userId=userId(content);
		doGet(httpClient,"http://login.bjfu.edu.cn/user/network/connect_action.jsp",
			"userid",userId,"ip",ip,"type","2");
	}

	static void disconnect(){
		//TODO  
	}
	
	static String userId(String html){
		Document doc=Jsoup.parse(html);
		Element elem=doc.select("frame#main").first();
		String src=elem.attr("src");
		Pattern pattern=Pattern.compile("userid=(\\d+)");
		Matcher matcher=pattern.matcher(src);
		String ans="";
		if(matcher.find()){
		  ans=matcher.group(1);
		}
		return ans;
	}
	
	static String cssQueryFirstText(String url,String cssQuery) throws Exception {
		String ip=null;
		Document doc=Jsoup.connect(url).get();
	  Elements elems=doc.select(cssQuery);
		Element elem=elems.first(); 
		ip=elem.text();
		return ip;
	}

	static String doGet(HttpClient httpClient, String url, String... pairs) 
	 throws Exception{
		String entityContent=null;
    url = makeGetSrl(url, pairs);
    HttpGet get = new HttpGet(url);
    HttpResponse response = httpClient.execute(get);
    //print("%d", response.getStatusLine().getStatusCode());
    entityContent = entity(response);
    //EntityUtils.consume(response.getEntity());
		return entityContent;
	}
	
	static boolean isConnectUrl1(String url) {
		boolean ans=true;
		try {
			Socket s=new Socket();
			SocketAddress address=new InetSocketAddress(url,80);
			s.connect(address,5000);
		} catch (Exception e) {
			e.printStackTrace();
			ans=false;
		}
		return ans;
	}
	
	static boolean isConnectUrl(String urlStr) {
		boolean ans=true;
		try{
		  URL url=new URL(urlStr);
		  HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		  conn.setConnectTimeout(3*1000);
		  conn.setRequestMethod("GET");
		  InputStream is=conn.getInputStream();
		}catch(Exception e){
			ans=false;
		}
		return ans;
	}
	
	static void printEntity(HttpResponse rp){
		print("%s",entity(rp));
	}

	static String entity(HttpResponse response) {
		StringBuilder sb = new StringBuilder("");
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	static String makeGetSrl(String url,String... pairs) {
		if(!url.endsWith("?")){
			url+="?";
		}
		List<NameValuePair>params=new LinkedList<NameValuePair>();
		int len=pairs.length;
		for(int i=0;i<len/2;i++){
			params.add(new BasicNameValuePair(pairs[2*i],pairs[2*i+1]));
		}
		String paramsStr=URLEncodedUtils.format(params,"utf-8");
		url+=paramsStr;
		return url;
	}

	static int doPost(HttpClient httpClient, String url, String... pairs) 
	 throws Exception{
		int resultCode=0;
    HttpPost post = new HttpPost(url);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    for (int i = 0; i < pairs.length / 2; i++) {
    	params.add(new BasicNameValuePair(pairs[2 * i], pairs[2 * i + 1]));
    }
    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
    HttpResponse response = httpClient.execute(post);
    resultCode=response.getStatusLine().getStatusCode();
    String txt=entity(response);
    Util.log("%s",txt);
    post.abort();
		return resultCode;
	}
	
	private static String getCookies(HttpClient client) {
        StringBuilder sb = new StringBuilder();
        List<Cookie> cookies = ((AbstractHttpClient)
        		client).getCookieStore().getCookies();
        for(Cookie cookie: cookies)
            sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
        return sb.toString();
   }
}