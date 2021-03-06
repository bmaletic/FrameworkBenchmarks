package org.voovan;

import org.voovan.http.message.HttpStatic;
import org.voovan.http.server.HttpRequest;
import org.voovan.http.server.HttpResponse;
import org.voovan.http.server.HttpRouter;
import org.voovan.http.server.WebServer;
import org.voovan.http.server.context.WebContext;
import org.voovan.http.server.context.WebServerConfig;
import org.voovan.tools.TObject;
import org.voovan.tools.json.JSON;
import org.voovan.tools.log.Logger;


public class VoovanTFB {
    private static final byte[] HELLO_WORLD = "Hello, World!".getBytes();

    public static void main(String[] args) {
        WebServerConfig webServerConfig = WebContext.getWebServerConfig();
	webServerConfig.setGzip(false);
	webServerConfig.setAccessLog(false);
	webServerConfig.setKeepAliveTimeout(20000);
	webServerConfig.setHost("0.0.0.0");
	webServerConfig.setPort(8080);
	webServerConfig.setCache(true);
	webServerConfig.getModuleonfigs().clear();
	webServerConfig.getRouterConfigs().clear();
        WebServer webServer = WebServer.newInstance(webServerConfig);
        Logger.setEnable(false);

        //性能测试路由
        webServer.get("/plaintext", new HttpRouter() {
            public void process(HttpRequest req, HttpResponse resp) throws Exception {
                resp.header().put(HttpStatic.CONTENT_TYPE_STRING, "text/plain");
                resp.header().remove(HttpStatic.CONNECTION_STRING);
                resp.write(HELLO_WORLD);
            }
        });

        //性能测试路由
        webServer.get("/json", new HttpRouter() {
            public void process(HttpRequest req, HttpResponse resp) throws Exception {
                resp.header().put("Content-Type", "application/json");
                resp.write(JSON.toJSON(TObject.asMap("message", "Hello, World!")));
            }
        });

        webServer.serve();
    }
}
