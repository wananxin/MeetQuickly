package com.andy.meetquickly.view.plugin;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/9 18:13
 * 描述：
 */
public class MyExtensionModule extends DefaultExtensionModule {


    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules =  new ArrayList<>();
        pluginModules.add(new MyPicPlugin());
        pluginModules.add(new GiftPlugin());
        pluginModules.add(new UserCardPlugin());
        return pluginModules;
    }

}
