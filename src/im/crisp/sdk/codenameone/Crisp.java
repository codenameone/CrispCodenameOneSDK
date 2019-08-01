/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Codename One through http://www.codenameone.com/ if you 
 * need additional information or have any questions.
 */

package im.crisp.sdk.codenameone;

import com.codename1.components.FloatingActionButton;
import com.codename1.io.Log;
import com.codename1.io.Preferences;
import com.codename1.io.Util;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.BrowserNavigationCallback;
import com.codename1.ui.layouts.BorderLayout;
import java.io.IOException;

/**
 * Bindings for Crisp SDK based on the crisp Android API
 *
 * @author Shai Almog
 */
public class Crisp {
    private static Crisp instance;
    private String tokenId;
    private Form chatForm;
    private Form previousForm;
    private BrowserComponent cmp;
    private String websiteId;
 
    private Crisp(String websiteId, ActionListener onLoaded) {
        this.websiteId = websiteId;
        tokenId = Preferences.get("crisp-token-id", (String)null);
        if(tokenId == null) {
            tokenId = websiteId + System.currentTimeMillis();
            Preferences.set("crisp-token-id", tokenId);
        }
        chatForm = new Form(new BorderLayout());

        String w = getProperty("BrowserComponent.useWKWebView", null);
        setProperty("BrowserComponent.useWKWebView", "true");
        cmp = new BrowserComponent();
        cmp.addWebEventListener(BrowserComponent.onLoad, onLoaded);
        cmp.setProperty("UseWideViewPort", true);
        cmp.setProperty("LoadWithOverviewMode", true);
        cmp.setProperty("DatabaseEnabled", true);
        cmp.setProperty("BuiltInZoomControls", true);
        cmp.setProperty("DisplayZoomControls", false);
        cmp.setProperty("WebContentsDebuggingEnabled", true);
        cmp.setFireCallbacksOnEdt(true);
        cmp.setURL("https://go.crisp.chat/chat/embed/?website_id=" + websiteId);
        cmp.addBrowserNavigationCallback(new BrowserNavigationCallback() {
            @Override
            public boolean shouldNavigate(String url) {
                if(!url.startsWith("file") && !url.startsWith("https://go.crisp.chat/chat/embed")) {
                    execute(url);
                    return false;
                }
                return true;
            }
        });
        chatForm.add(CENTER, cmp);
        chatForm.getToolbar().setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW,
            e ->  previousForm.showBack());
        setProperty("BrowserComponent.useWKWebView", w);
    }

    private Crisp(Builder b) {
        instance = this;
        this.websiteId = b.websiteId;
        tokenId = Preferences.get("crisp-token-id", (String)null);
        if(tokenId == null) {
            tokenId = websiteId + System.currentTimeMillis();
            Preferences.set("crisp-token-id", tokenId);
        }
        chatForm = new Form(new BorderLayout());

        String w = getProperty("BrowserComponent.useWKWebView", null);
        setProperty("BrowserComponent.useWKWebView", "true");
        cmp = new BrowserComponent();
        cmp.setProperty("UseWideViewPort", true);
        cmp.setProperty("LoadWithOverviewMode", true);
        cmp.setProperty("DatabaseEnabled", true);
        cmp.setProperty("BuiltInZoomControls", true);
        cmp.setProperty("DisplayZoomControls", false);
        cmp.setProperty("WebContentsDebuggingEnabled", true);
        cmp.setFireCallbacksOnEdt(true);
        
        String url = "https://go.crisp.chat/chat/embed/?website_id=" + websiteId;
        
        if(b.email != null) {
            url += "&user_email=" + Util.encodeUrl(b.email);
        }

        if(b.avatar != null) {
            url += "&user_avatar=" + Util.encodeUrl(b.avatar);
        }
        
        if(b.nickname != null) {
            url += "&user_nickname=" + Util.encodeUrl(b.nickname);
        }

        if(b.phone != null) {
            url += "&user_phone=" + Util.encodeUrl(b.phone);
        }

        cmp.setURL(url);
        cmp.addBrowserNavigationCallback(new BrowserNavigationCallback() {
            @Override
            public boolean shouldNavigate(String url) {
                if(!url.startsWith("file") && !url.startsWith("https://go.crisp.chat/chat/embed")) {
                    execute(url);
                    return false;
                }
                return true;
            }
        });
        chatForm.add(CENTER, cmp);
        chatForm.getToolbar().setBackCommand("", Toolbar.BackCommandPolicy.AS_ARROW,
            e ->  previousForm.showBack());
        setProperty("BrowserComponent.useWKWebView", w);
    }
    
    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public static void init(String websiteId, ActionListener onLoaded) {
        instance = new Crisp(websiteId, onLoaded);
    }
    
    public static Crisp getInstance() {
        return instance;
    }

    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * Builder pattern for Crisp instances. Until build is invoked instance 
     * will be null.
     */
    public static Builder init(String websiteId) {
        Builder b = new Builder();
        b.websiteId = websiteId;
        return b;
    }

    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public void setEmail(String email) {
        cmp.execute("window.$crisp.push([\"set\", \"user:email\", [\"" + email + "\"]])");
    }

    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public void setNickname(String nickname) {
        cmp.execute("window.$crisp.push([\"set\", \"user:nickname\", [\"" + nickname + "\"]])");
    }

    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public void setPhone(String phone) {
        cmp.execute("window.$crisp.push([\"set\", \"user:phone\", [\"" + phone + "\"]])");
    }

    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public void setAvatar(String avatar) {
        cmp.execute("window.$crisp.push([\"set\", \"user:avatar\", [\"" + avatar + "\"]])");
    }

    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public void setData(String key, String value) {
        cmp.execute("window.$crisp.push([\"set\", \"session:data\", [\"" + key + "\", \"" + value + "\"]])");
    }

    /**
     * @deprecated this API doesn't work properly in the JavaScript port. You should
     * use {@link #init(java.lang.String)} instead
     */
    public void setSegments(String segment) {
        cmp.execute("window.$crisp.push([\"set\", \"session:segments\", [[\"" + segment + "\"]]])");
    }

    public void reset() {
        Preferences.delete("crisp-token-id");
        tokenId = websiteId + System.currentTimeMillis();
        Preferences.set("crisp-token-id", tokenId);
        cmp.reload();
    }
    
    public void openChat() {
        previousForm = getCurrentForm();
        chatForm.show();
    }
    
    public FloatingActionButton chatFab() {
        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_CHAT);
        fab.addActionListener(e -> openChat());
        return fab;
    }
    
    public void bindFab(Form f) {
        chatFab().bindFabToContainer(f);
    }
    
    
    /**
     * The builder class is used to create an instance of the crisp object in 
     * a portable way
     */
    public static class Builder {
        private String websiteId;
        private String email;
        private String phone;
        private String nickname;
        private String avatar;
        
        public Builder websiteId(String websiteId) {
            this.websiteId = websiteId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }
        
        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }
        
        public Crisp build() {
            return new Crisp(this);
        }
    }
}
