# monicom

A basic and straight forward and universal serial monitor. Something a bit
better than the [Arduino IDE](https://www.arduino.cc/en/Main/Software)
serial monitor, more professional, but not as daunting as
[RealTerm](https://realterm.sourceforge.io/). Also this program aims to be
cross-platform, so you can have the same serial experience no matter which
OS you are using.


## Installing

First of all head to the [releases page](https://github.com/nathanpc/monicom/releases) and download the latest `zip` release and unpack it.

To install this program on Windows simply put it somewhere permanent like the `Program Files` folder and create a shortcut to the `monicom.exe` executable in your Start Menu.

If you're running Linux just run the following commands:

```bash
sudo cp -r ~/Downloads/monicom/ /opt
cd /usr/share/applications
sudo ln -s /opt/monicom/monicom.desktop
```


## Installing RXTX

This project uses the [RXTX Java library](http://rxtx.qbang.org/wiki/index.php/Main_Page), which needs to be present in your system in order for it to work.

If you're using Windows, just follow [this tutorial](http://rxtx.qbang.org/wiki/index.php/Installation_for_Windows) and you'll quickly get it up and running.

If you're using Mac OS X, just follow the Installing Binaries section of [this tutorial](http://rxtx.qbang.org/wiki/index.php/Installation_on_MacOS_X) ans you should be all set.

In case you're running Debian or Ubuntu you can install it and configure the binaries required by running the following commands:

```bash
sudo apt install librxtx-java
sudo cp -r /usr/lib/jni/librxtx* /usr/lib/
```


## Screenshot

This is a Java application, but it will try to get the native theme if possible so that it doesn't have that "java app look" even on Linux.

![Screenshot](https://i.imgur.com/6WrIgEF.png)

