package com.showu.baogu.util.report;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.showu.baogu.api.AddReportUser;
import com.showu.baogu.application.Const;
import com.showu.baogu.xmpp.provider.SessionManager;
import com.showu.common.http.HttpClient;
import com.showu.common.http.HttpStringResult;
import com.showu.common.http.IResponse;
import com.showu.common.util.FileUtil;
import com.showu.common.util.GUIUtil;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class Report implements IResponse {
    protected Context mContext;
    protected String userId;
    protected ReportType reportType;
    protected ReportReson reportReson;
    private SessionManager sessionManager;
    private HttpClient client;
    private int pullBack = 1;
    private AddReportUser addReportUser = new AddReportUser();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 上传文件成功
                    Bundle bundle = msg.getData();
                    String remoteUrl = bundle.getString("remoteUrl");
                    uploadFileSuccess(remoteUrl);
                    break;
                case 1:// 举报成功
                    reportSuccess();
                    break;
                case 2:// 拉黑了爆谷用户
                    blackBaogu();
                    break;
                case 3:// 举报失败
                    reportFail();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public Report(Context context, ReportType reportType, ReportReson reson,
                  String userId) {
        this.mContext = context;
        this.reportType = reportType;
        this.userId = userId;
        this.reportReson = reson;
        client = new HttpClient(this);
        sessionManager = SessionManager.getInstance(context);
    }

    protected void uploadFileSuccess(String fileUrl) {
        addReportUser.setDeviceidentifyid(GUIUtil.getDeviceId(mContext));
        addReportUser.setReftype(reportType.toString());
        addReportUser.setUploadfile(fileUrl);
        addReportUser.setUseridto(userId);
        addReportUser.setUserid(Const.user.getUserid());
        //String url = Const.json_api_url + addReportUser.getApi();
        String url = Const.urlBean.getJson_api_url() + addReportUser.getApi();
        client.post(url, addReportUser.postString());
//		Map<String, String> param = createParam(fileUrl);
//		String url = Const.JSON_API_URL
//				+ mContext.getString(R.string.add_report);
//		new HttpClientBaogu(url, this, 0).execute(param, 1);
    }

    ;

//	protected Map<String, String> createParam(String url) {
//
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("useridfrom", Const.user.getUserid());
//		param.put("useridto", userId);
//		param.put("reason", reportReson + "");
//		param.put("pullblack", pullBack + "");
//		param.put("uploadfile", url);
//		param.put("refid", reportReson + "");
//		param.put("reftype", reportType + "");
//		return param;
//	};

    protected void reportSuccess() {
        GUIUtil.toast(mContext,"举报成功");
        StringBuffer sb = new StringBuffer("有人举报你");
        if (reportType == ReportType.SEXINFO) {
            sb.append("色情信息");
        } else if (reportType == ReportType.REACTIONARY_POLITICAL) {
            sb.append("反动政治");
        } else if (reportType == ReportType.ADS_CHEAT) {
            sb.append("广告欺诈");
        } else if (reportType == ReportType.SCOLD) {
            sb.append("谩骂骚扰");
        } else if (reportType == ReportType.CHEAT_MESSAGE) {
            sb.append("伪造他人言论");
        } else if (reportType == ReportType.RUMORBOOING) {
            sb.append("造摇起哄");
        } else if (reportType == ReportType.USERINFO) {
            sb.append("个人资料不当");
        } else if (reportType == ReportType.USER_OTHRE_INFO) {
            sb.append("盗用他人资料");
        }
//		MessageUtil.sendSystemMessage(mContext, userId, sb.toString());
        Toast.makeText(mContext, "已经收到了你的举报信息", Toast.LENGTH_SHORT).show();
        initMessage();
    }

    ;

    private void initMessage() {
//		BaseMessage message = MessageUtil
//				.constructMessage(
//						"10000",
//						"你好，感谢你的举报，爆谷将进一步审核，经核实，爆谷将封禁TA的设备。");
//        BaseMessage message = MessageUtil.getInstance().createSendMessage()

//		message.setMessageTarget(MessageSRC.FROM);
//		MessageListBean listBean = new MessageListBean();
//		listBean.setContent(message.getBody());
//		listBean.setCount(1);
//		listBean.setDistanc("0");
//		listBean.setTargetId("10000");
//		listBean.setType(Type.MESSAGE);
//		listBean.setName("爆谷电影");
//		listBean.setTime(message.getBaogu_send_time());
//		sessionManager.saveOrUpdateSession(listBean, MessageTarget.FROM);
//		LogUtil.i(getClass(), message.getTargetId());
//		DBHelper.getInstance(mContext).insert(
//				BaseMessage.class.getSimpleName(), null,
//				message.persistence());
    }

    public void report(File file, int pullBack) {
        this.pullBack = pullBack;
        //String url = Const.upload_api_url
        String url = Const.urlBean.getUpload_api_url()
                + "/HttpHandlers/UploadFilesFromIphoneClient.ashx?filetype=5";
        client.uploadFile(url, file);
    }

    public void report(String reportContent, int pllBack) {
        File file = FileUtil.getNewFile(mContext, UUID.randomUUID()
                .toString() + ".txt");
        try {
            FileWriter fos = new FileWriter(file);
            fos.write(reportContent);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (file != null) {
            report(file, pullBack);
        }
    }

    protected void blackBaogu() {
//		Toast.makeText(mContext, mContext.getString(R.string.black_baogu),
//				Toast.LENGTH_SHORT).show();
    }

    protected void reportFail() {
//		Toast.makeText(mContext, mContext.getString(R.string.report_failed),
//				Toast.LENGTH_SHORT).show();
    }

//	private IFileUpload ifileUpload = new IFileUpload() {
//
//		@Override
//		public void uploading(String url, int len) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void uploadError(String url, int code, String errorMessage) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void uploadEnd(String url, String content) {
//			try {
//				JSONObject jsonObject = new JSONObject(content);
//				int status = jsonObject.getInt("status");
//				if (status == 1) {
//					String remoteUrl = jsonObject.getString("httpurls");
//					Bundle b = new Bundle();
//					b.putString("url", url);
//					b.putString("remoteUrl", remoteUrl);
//					Message msg = new Message();
//					msg.what = 0;
//					msg.setData(b);
//					handler.sendMessage(msg);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//		@Override
//		public void startUpload(String url, int fileSize) {
//			// TODO Auto-generated method stub
//
//		}
//	};

    public void requestSuccess(String url,
                               String response, int page) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if (status == 1) {// 举报成功
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            } else if (status == 5) {// 拉黑了爆谷用户
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            } else {// 举报失败
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    ;


    @Override
    public void response(HttpStringResult result) {
        if (result.getStatus() == 200) {
            if (result.getUrl().contains(addReportUser.getApi())) {
                try {
                    JSONObject jsonObject = new JSONObject(result.getJson());
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                      reportSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result.getJson());
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        String remoteUrl = jsonObject.getString("httpurls");
                        uploadFileSuccess(remoteUrl);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
