package com.andy.meetquickly.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;

import com.andy.meetquickly.common.Constant;
import com.andy.meetquickly.message.wechartSuccessMessage;
import com.andy.meetquickly.utils.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.vondear.rxtool.view.RxToast;

import org.greenrobot.eventbus.EventBus;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/17 16:51
 * 描述：
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constant.WECHART_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0){
                RxToast.showToast("支付成功");
                EventBus.getDefault().post(new wechartSuccessMessage(""));
                finish();
            }else if (resp.errCode == -1){
                RxToast.showToast("支付错误");
                finish();
            }else if (resp.errCode == -2){
                RxToast.showToast("取消支付");
                finish();
            }
        }
    }
}
