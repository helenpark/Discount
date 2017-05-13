package com.td.mobile.nextgen.restful;

import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.Iterator;

public class RestResponse {
	private JSONObject raw;
	private Object payload;
	private String name;
	private String messageID;
	private String statusCode;
	private String serverStatusCode;
	private String Severity;

	private String httpError;

	public RestResponse(JSONObject errorObj){
		super();
		try {
			raw = errorObj;	
			TDLog.d(Consts.LOG_TAG, "RestREesponse, errorObj=" + raw);
			setHttpError(errorObj.getString("HttpError"));
		} catch (Exception e) {
			TDLog.d(Consts.LOG_TAG, e.getMessage(), e);
		}
	}

	public RestResponse(String s, HttpStatus status) {
		super();
		try {
			TDLog.d(Consts.LOG_TAG, "HttpStatus=" + status + " result=" + s);
			if(HttpStatus.OK==status){
				raw = new JSONObject(s);
				parseResponse();
			} else {
				setHttpError(String.valueOf(status));
			}
			
		} catch (Exception e) {
			TDLog.d(Consts.LOG_TAG, e.getMessage(), e);
		}
	}

	private void parseResponse() throws Exception {
		JSONObject aRaw = getRaw();
		if (aRaw != null) {
			Iterator iter = aRaw.keys();
			String s = (iter.hasNext()) ? (String) iter.next() : null;
			if (s != null) {
				setName(s);
				JSONObject resp = aRaw.getJSONObject(s);
				if (resp != null) {
					setPayload(resp);
				}
				Iterator contentIter = resp.keys();
				while (contentIter.hasNext()) {
					String aKey = (String) contentIter.next();
					if ("MessageID".equalsIgnoreCase(aKey)) {
						setMessageID(resp.getString("MessageID"));
					} else if ("Status".equalsIgnoreCase(aKey)) {
						JSONObject status = resp.getJSONObject("Status");
						if (status != null) {
							setStatusCode(status.getString("StatusCode"));
							setSeverity(status.getString("Severity"));
							try {
								JSONArray additionalStatus=status.getJSONArray("AdditionalStatus");
								if(additionalStatus!=null && additionalStatus.isNull(0)==false){
									JSONObject errorStatus=additionalStatus.getJSONObject(0);
									String sErrorCode=errorStatus.getString("ServerStatusCode");
									setServerStatusCode(sErrorCode);								
								}
							} catch (Exception e) {
							}
							
						}
					}
				}
			}
		}
	}

	public JSONObject getRaw() {
		return raw;
	}

	public void setRaw(JSONObject raw) {
		this.raw = raw;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getSeverity() {
		return Severity;
	}

	public void setSeverity(String severity) {
		Severity = severity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getHttpError() {
		return httpError;
	}

	public void setHttpError(String httpError) {
		this.httpError = httpError;
	}

	public boolean hasHttpError(){
		return httpError!= null;
	}
	
	public boolean hasError(){
		return  hasHttpError() || getServerStatusCode()!=null || !(getStatusCode().equals("0"));
	}

	public String getServerStatusCode() {
		return serverStatusCode;
	}

	public void setServerStatusCode(String serverStatusCode) {
		this.serverStatusCode = serverStatusCode;
	}
}
