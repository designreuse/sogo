package com.yihexinda.dataservice.pay;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/7 0007
 */
public class MyConfig implements WXPayConfig {

    private byte[] certData;

    public MyConfig() throws Exception {
        String certPath = "";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            certPath =  "E:\\apiclient_cert.p12";
        }else{
            certPath =  "/data/cert/apiclient_cert.p12";
        }
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return "wx1a5b0ee65f79eded";
    }

    public String getMchID() {
        return "1519768271";
    }

    public String getKey() {
        return "b25af07c17f74dd38fec10ad2eedd0a3";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}

