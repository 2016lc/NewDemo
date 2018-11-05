package com.jiudu.newdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RandomAccessFile Random = null;
    FileChannel Channel = null;
    TextView tvTitle = null;
    DemoView demoView  = null;

    /**
     * 获取当前手机上的应用商店数量
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getInstalledMarketPkgs(Context context) {
        ArrayList<String> pkgs = new ArrayList<>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();

        intent.setAction("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("market://details?id="));
        PackageManager pm = context.getPackageManager();
        // 通过queryIntentActivities获取ResolveInfo对象
        List<ResolveInfo> infos = pm.queryIntentActivities(intent,
                0);
        if (infos == null || infos.size() == 0)
            return pkgs;
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName))
                pkgs.add(pkgName);
        }
        return pkgs;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = findViewById(R.id.tv_title);
        demoView = findViewById(R.id.dv_view);
        demoView.setTextColor(12);
        demoView.setPaintColor(getResources().getColor(R.color.colorAccent));
//        TelephonyManager telephonyManager = (TelephonyManager)
//                getSystemService(Context.TELEPHONY_SERVICE);
//        String e = String.format("-%s-%s-", "openinstall", "yyb");
//        String path2 = getApplicationContext().getPackageResourcePath();
//        getMessge(path2);
        List<String> listpath = getInstalledMarketPkgs(this);
        StringBuffer  buffer = new StringBuffer();
        for (String path:listpath){
            buffer.append("应用市场：");
            buffer.append(path);
            buffer.append(",");
        }
        tvTitle.setText(buffer.toString());

//        tvTitle.setText("app的名称:"+SystemUtil.AppName()+"\n手机的IMEI号:"+SystemUtil.IMEI()
//        +"\n手机的IMSI:"+SystemUtil.IMSI()+"\n手机的号码:"+SystemUtil.Num()+"\n手机产品的序列号:"+SystemUtil.SN()+"\n手机的sim号:"+SystemUtil.SIM()
//                + "\n手机的ID:"+ SystemUtil.ID()+"\n手机的mac地址:"+SystemUtil.MAC()+"\n系统国家:"+SystemUtil.Country()
//                +"\n系统语言:"+ SystemUtil.Language()+"\n系统版本名:"+ Build.VERSION.RELEASE+"\n系统版本号:"+Build.VERSION.SDK_INT
//        +"\n系统型号:"+Build.MODEL+"\n系统定制商:"+Build.BRAND+"\n系统的主板:"+Build.BOARD+"\n手机制造商:"+Build.PRODUCT
//        +"\n系统2:"+Build.HOST+"\n系统硬件制造商:"+Build.MANUFACTURER+"\nCPU名称:"+getCpuName());
//        tvTitle.setText(readAPK(new File(getPackagePath(this))));
//        getMessge(getAPPDir(this));
    }
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取应用安装路径
     * @param context
     * @return
     */
    private static String getAPPDir(@NonNull Context context) {
        String var1 = null;

        try {
            ApplicationInfo var2 = context.getApplicationInfo();
            if (var2 == null) {
                return null;
            }

            var1 = var2.sourceDir;
        } catch (Throwable var3) {
            ;
        }

        return var1;
    }

    public String readAPK(File file) {
        byte[] bytes = null;
        try {
            RandomAccessFile accessFile = new RandomAccessFile(file, "r");
            long index = accessFile.length();

            bytes = new byte[2];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);

            int contentLength = stream2Short(bytes, 0);

            bytes = new byte[contentLength];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);
            String a = new String(bytes, "utf-8");
            Log.d("Hello", a);
            return a;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static short stream2Short(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        return buffer.getShort(0);
    }

    public String getPackagePath(Context context) {
        if(context != null) {
            return context.getPackageCodePath();
        }
        return null;
    }


    /**
     * 获取渠道信息
     */
    private void getMessge(String appUrl){
        try {
            Random = new RandomAccessFile(new File(appUrl), "r");
            Channel = Random.getChannel();
            long namber = getFileChannerLength(Channel);
            Channel.position(namber-24L);
            // 创建缓冲区
            ByteBuffer buf = ByteBuffer.allocate(24);
            // 从通道读取数据到缓冲区
            Channel.read(buf);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            // 反转缓冲区(limit设置为position,position设置为0,mark设置为-1)
            buf.flip();
            // 就是判断position和limit之间是否有元素
            ByteBuffer butt = ByteBuffer.allocate(11);
            butt.put((byte)5);
            if (buf.getLong(8) == 2334950737559900225L && buf.getLong(16) == 3617552046287187010L) {
                long var4 = buf.getLong(0);
                if (var4 >= (long)buf.capacity() && var4 <= 2147483639L) {
                    int var6 = (int)(var4 + 8L);
                    long var7 = namber - (long)var6;
                    if (var7 < 0L) {

                    }else {

                        Channel.position(var7);

                        ByteBuffer var9 = ByteBuffer.allocate(var6);
                        Channel.read(var9);
                        var9.order(ByteOrder.LITTLE_ENDIAN);
                        System.out.println("----------------------");
                        Map var1 = getMap(var9);
                        ByteBuffer var3 = (ByteBuffer) var1.get(1114793335);
                        String messge = new String(ToByte(var3));
                        String type = messge.substring(messge.indexOf("{"));
//                        JSONObject object = new JSONObject(type);
//                        String sher = object.getString("shareInstallCode");
                        tvTitle.setText(type);
                    }

                }else {
                    System.out.print("55555");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static byte[] ToByte(ByteBuffer var0) {
        if(var0==null)
            return new byte[0];
        byte[] var1 = var0.array();
        int var2 = var0.arrayOffset();
        return Arrays.copyOfRange(var1, var2 + var0.position(), var2 + var0.limit());
    }

    /**
     * 获取getMAP
     * @param var0
     * @return
     */
    public static Map<Integer, ByteBuffer> getMap(ByteBuffer var0) {

        ByteBuffer var1 = getByteBuffer(var0, 8, var0.capacity() - 24);
        System.out.println("_______________________");

        LinkedHashMap var2 = new LinkedHashMap();
        int var3 = 0;

        while(var1.hasRemaining()) {
            ++var3;
            if (var1.remaining() < 8) {
//                throw new d("Insufficient data to read size of APK Signing Block entry #" + var3);
            }

            long var4 = var1.getLong();
            if (var4 < 4L || var4 > 2147483647L) {
//                throw new d("APK Signing Block entry #" + var3 + " size out of range: " + var4);
            }

            int var6 = (int)var4;
            int var7 = var1.position() + var6;
            if (var6 > var1.remaining()) {
//                throw new d("APK Signing Block entry #" + var3 + " size out of range: " + var6 + ", available: " + var1.remaining());
            }

            int var8 = var1.getInt();
            System.out.println("--------------------------");
            System.out.print( "apk:"+var8+"   ");
            var2.put(var8, getBuffer(var1, var6 - 4));
            var1.position(var7);
        }

        return var2;
    }

    private static ByteBuffer getByteBuffer(ByteBuffer var0, int var1, int var2) {

        if (var1 < 0) {
            throw new IllegalArgumentException("start: " + var1);
        } else if (var2 < var1) {
            throw new IllegalArgumentException("end < start: " + var2 + " < " + var1);
        } else {
            int var3 = var0.capacity();

            if (var2 > var0.capacity()) {
                throw new IllegalArgumentException("end > capacity: " + var2 + " > " + var3);
            } else {
                int var4 = var0.limit();
                int var5 = var0.position();
                ByteBuffer var7;
                try {
                    var0.position(0);
                    var0.limit(var2);
                    var0.position(var1);
                    ByteBuffer var6 = var0.slice();
                    var6.order(var0.order());
                    var7 = var6;
//                    System.out.println("users:"+new String(as(var6)));
                } finally {
                    var0.position(0);
                    var0.limit(var4);
                    var0.position(var5);
                }
                return var7;
            }
        }
    }


    private static ByteBuffer getBuffer(ByteBuffer var0, int var1) {
        if (var1 < 0) {
            throw new IllegalArgumentException("size: " + var1);
        } else {
            int var2 = var0.limit();
            int var3 = var0.position();
            int var4 = var3 + var1;
            if (var4 >= var3 && var4 <= var2) {
                var0.limit(var4);

                ByteBuffer var6;
                try {
                    ByteBuffer var5 = var0.slice();
                    var5.order(var0.order());
                    var0.position(var4);
                    var6 = var5;
                } finally {
                    var0.limit(var2);
                }

                return var6;
            } else {
                throw new BufferUnderflowException();
            }
        }
    }

    /**
     * 获取通道长度
     * @param channel
     * @return
     * @throws IOException
     */
    public static long getFileChannerLength(FileChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        channel.position(channel.size()  - 6L);
        channel.read(buffer);
        long code = (long)buffer.getInt(0);
        return code;
    }

    /**
     * MD5加密
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    /**
     * 获取app签名md5值
     */
    public String getSignMd5Str() {
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = encryptionMD5(sign.toByteArray());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
