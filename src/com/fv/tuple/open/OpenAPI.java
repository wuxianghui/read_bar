package com.fv.tuple.open;


public class OpenAPI {
	//field from net
	public final static String FIELD_USER_NAME="name";
	public final static String FIELD_USER_EMAIL="email";
	public final static String FIELD_USER_PASSWORD="password";
	public final static String FIELD_USER_JOB="job";
	public final static String FIELD_USER_SEX="age";
	public final static String FIELD_USER_AGE="address";
	public final static String FIELD_USER_ADDRESS="school";
	public final static String FIELD_USER_SCHOOL="comments";
	public final static String FIELD_USER_COMMENTS="signature";
	public final static String FIELD_USER_SIGNATURE="signature";
	public final static String FIELD_USER_IS_AUTHENTICATED="is_authenticated";
	public final static String FIELD_USER_STATE="state";
	public final static String FIELD_USER_LATITUDE="latitude";
	public final static String FIELD_USER_LONGITUDE="longitude";
	public final static String FIELD_USER_PHOTO="photo";

	///below is for main server ///////////
	static OpenAPI mOpenAPI = null;
	//private  String URL_API_MAIN="http://192.168.1.103:8124";
	private  String URL_API_MAIN="http://192.168.1.103:8124";
    private  String URL_API_REGISTER="register";
    private  String URL_API_LOGIN="login";
    private  String URL_API_GET_ROOT_TECH_MENU="get_root_tech_menu";
    private  String URL_API_GET_TECH_MENU="get_tech_menu";
    
    private  String URL_API_GET_ME="get_me";
    private  String URL_API_REQUEST_ADD_FRIEND="request_add_friend";
    private  String URL_API_UPDATE_USER_TECH="update_user_tech";
    private  String URL_API_UPDATE_USER_STUDY="update_user_study";
    
    
    private  String URL_API_UPDATE_BASIC="update_basic";
    private  String URL_API_UPDATE_SIGNATRUE="update_signature";
    
    private  String URL_API_UPDATE_PASSWORD="update_password";
    
    private  String URL_API_GET_USER_BY_TECH="get_user_by_tech";
    private  String URL_API_GET_USER_BY_STUDY="get_user_by_study";
    
    private  String URL_API_ADD_REQUEST_INFO="add_request_info";
    private  String URL_API_GET_REQUEST_INFO="get_study_request_info";
    
    private  String URL_API_GET_TECH_ASSESS="get_tech_assess";
    ///////////////////////////////////////
	public static OpenAPI getInstance() {
		if (mOpenAPI == null) {
			mOpenAPI = new OpenAPI();
		}
		return mOpenAPI;
	}
	public String getRegisterUrl()
	{
		return URL_API_MAIN+"/"+URL_API_REGISTER;
	}
	public String getUpdatePasswordUrl()
	{
		return URL_API_MAIN+"/"+URL_API_UPDATE_PASSWORD;
	}
	public String getUpdateBasicUrl()
	{
		return URL_API_MAIN+"/"+URL_API_UPDATE_BASIC;
	}
	public String getUpdateSignatureUrl()
	{
		return URL_API_MAIN+"/"+URL_API_UPDATE_SIGNATRUE;
	}
	
	public String getMeUrl()
	{
		return URL_API_MAIN+"/"+URL_API_GET_ME;
	}
	
	public String getRequestAddFriend()
	{
		return URL_API_MAIN+"/"+URL_API_REQUEST_ADD_FRIEND;
	}
	
	public String getUpdateUserTechUrl()
	{
		return URL_API_MAIN+"/"+URL_API_UPDATE_USER_TECH;
	}
	
	public String getUpdateUserStudyUrl()
	{
		return URL_API_MAIN+"/"+URL_API_UPDATE_USER_STUDY;
	}
	
	public String getLoginUrl()
	{
		return URL_API_MAIN+"/"+URL_API_LOGIN;
	}
	public String getGetRootTechMenu()
	{
		return URL_API_MAIN+"/"+URL_API_GET_ROOT_TECH_MENU;
	}
	public String getGetTechMenu()
	{
		return URL_API_MAIN+"/"+URL_API_GET_TECH_MENU;
	}
	public String getGetUserByTech()
	{
		return URL_API_MAIN+"/"+URL_API_GET_USER_BY_TECH;
	}
	public String getGetUserByStudy()
	{
		return URL_API_MAIN+"/"+URL_API_GET_USER_BY_STUDY;
	}
	public String getAddRequestInfoUrl()
	{
		return URL_API_MAIN+"/"+URL_API_ADD_REQUEST_INFO;
	}
	public String getGetRequestInfo()
	{
		return URL_API_MAIN+"/"+URL_API_GET_REQUEST_INFO;
	}
	public String getGetTechAssess()
	{
		return URL_API_MAIN+"/"+URL_API_GET_TECH_ASSESS;
	}
	
	///below is other server ip/////
	//field from net
	public final static String FIELD_IMAGE_SERVER_IP="image_server_ip";
	public final static String FIELD_VIDEO_SERVER_IP="video_server_ip";
	public final static String FIELD_MESSAGE_SERVER_IP="message_server_ip";
    private  String URL_IMAGE_SERVER="http://192.168.1.100:8124";
    private  String URL_VIDEO_SERVER="http://192.168.1.100:8125";
    private  String URL_MESSAGE_SERVER="http://192.168.1.100:8126";
    
    public final static String SUB_SYSTEM_IMAGE_FOLDER="system";
    public void setImageServerUrl(String url)
    {
    	URL_IMAGE_SERVER=url;	
    }
    public void setVideoServerUrl(String url)
    {
    	URL_VIDEO_SERVER=url;	
    }
    public void setMessageServerUrl(String url)
    {
    	URL_MESSAGE_SERVER=url;	
    }
    
    public String getImageServerUrl()
    {
    	return URL_IMAGE_SERVER;	
    }
    
    public String getSystemImageUrl(String image)
    {
    	return URL_IMAGE_SERVER+"/"+SUB_SYSTEM_IMAGE_FOLDER+"/"+image;	
    }
    
    public String getVideoServerUrl()
    {
    	return URL_VIDEO_SERVER;	
    }
    public String getMessageServerUrl( )
    {
    	return URL_MESSAGE_SERVER;	
    }
    //////////////////////////

}
