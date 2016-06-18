/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Rojoss <http://jroossien.com>
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.jroossien.boxx.commands;

import com.jroossien.boxx.Boxx;
import com.jroossien.boxx.commands.api.*;
import com.jroossien.boxx.commands.api.data.ArgRequirement;
import com.jroossien.boxx.commands.api.parse.SubCmdO;
import com.jroossien.boxx.messages.*;
import com.jroossien.boxx.options.single.PlayerO;
import com.jroossien.boxx.options.single.StringO;
import com.jroossien.boxx.util.ItemUtil;
import com.jroossien.boxx.util.Str;

import java.io.File;
import java.util.Set;

public class BoxxCmd extends BaseCmd {

    public BoxxCmd(File file) {
        super("boxx", "box");
        file(file);
        desc("Main Boxx command.");

        addArgument("action", ArgRequirement.REQUIRED, new SubCmdO(new Info(this), new Reload(this), new Lang(this), new Wand(this))).desc("A sub command.");
    }

    @Override
    public void onCommand(CmdData data) {}


    public class Info extends SubCmd<BoxxCmd> {
        private Info(BoxxCmd gameBoxxCmd) {
            super(gameBoxxCmd, "info", "plugin", "details", "detail");
            desc("Display plugin information.");
        }

        @Override
        public void onCommand(CmdData data) {
            data.getSender().sendMessage(Str.color("&8===== &4&lBoxx &8=====\n" +
                    "&6&lAuthors&8&l: &a" + Str.implode(getBoxx().getDescription().getAuthors(), ", ", " & ") + "\n" +
                    "&6&lVersion&8&l: &a" + getBoxx().getDescription().getVersion()));
        }
    }


    public class Reload extends SubCmd<BoxxCmd> {
        private Reload(BoxxCmd gameBoxxCmd) {
            super(gameBoxxCmd, "reload", "load");
            desc("Reload all the configuration files including messages and such.");
            perm("boxx.cmd.reload");
        }

        @Override
        public void onCommand(CmdData data) {
            getBoxx().getCfg().load();

            getBoxx().setupLanguage();
            for (MessageConfig cfg : MessageConfig.getConfigs()) {
                cfg.loadFull();
            }

            Set<BaseCmd> cmds = CmdRegistration.getCommands(Boxx.class);
            if (cmds != null) {
                for (BaseCmd cmd : cmds) {
                    cmd.load();
                }
            }

            Msg.get("boxx.reloaded", Param.P("type", "all")).send(data.getSender());
        }
    }


    public class Lang extends SubCmd<BoxxCmd> {
        private Lang(BoxxCmd gameBoxxCmd) {
            super(gameBoxxCmd, "language", "lang", "locale");
            desc("Get or set the language used for messages.");
            perm("boxx.cmd.language");

            addArgument("language", ArgRequirement.OPTIONAL, new StringO().match(Language.getAliases())).desc("The language name to set.").perm("boxx.cmd.language.set");
        }

        @Override
        public void onCommand(CmdData data) {
            if (data.hasArg("language")) {
                Language lang = Language.find((String)data.getArg("language"));

                getBoxx().getCfg().language = lang.getID();
                getBoxx().getCfg().save();
                getBoxx().setupLanguage();

                for (MessageConfig config : MessageConfig.getConfigs()) {
                    config.loadFull();
                }

                Msg.get("boxx.language.set", Param.P("language", lang.getName())).send(data.getSender());
                return;
            }

            Msg.get("boxx.language.get", Param.P("language", getBoxx().getLanguage().getName())).send(data.getSender());
        }
    }

    public class Wand extends SubCmd<BoxxCmd> {
        private Wand(BoxxCmd gameBoxxCmd) {
            super(gameBoxxCmd, "wand", "swand", "selwand", "selectionwand");
            desc("Give the cuboid selection wand.");
            perm("boxx.cmd.wand");

            addArgument("player", ArgRequirement.REQUIRED_NON_PLAYER, new PlayerO()).desc("A player to give the wand to.");
        }

        @Override
        public void onCommand(CmdData data) {
            ItemUtil.add(data.getPlayer("player").getInventory(), getBoxx().getSM().getWand(), true);
            Msg.get("wand.given").send(data.getSender());
        }
    }
}
