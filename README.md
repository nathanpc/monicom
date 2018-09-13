# monicom

A basic and straight forward and universal serial monitor. Something a bit
better than the [Arduino IDE](https://www.arduino.cc/en/Main/Software)
serial monitor, more professional, but not as daunting as
[RealTerm](https://realterm.sourceforge.io/). Also this program aims to be
cross-platform, so you can have the same serial experience no matter which
OS you are using.

## Installing RXTX

This project uses the [RXTX Java library](http://rxtx.qbang.org/wiki/index.php/Main_Page), which needs to be present in your system in order for it to work.

If you're using Windows, just follow [this tutorial](http://rxtx.qbang.org/wiki/index.php/Installation_for_Windows) and you'll quickly get it up and running.

If you're using Mac OS X, just follow the Installing Binaries section of [this tutorial](http://rxtx.qbang.org/wiki/index.php/Installation_on_MacOS_X) ans you should be all set.

In case you're running Debian or Ubuntu you can install it and configure the binaries required by running the following commands:

```bash
sudo apt install librxtx-java
sudo cp -r /usr/lib/jni/librxtx* /usr/lib/
```

