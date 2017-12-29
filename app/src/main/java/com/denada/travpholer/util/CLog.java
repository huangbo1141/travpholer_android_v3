package com.denada.travpholer.util;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CLog
{
	public	static	final	String strLogFileName	= Environment.getExternalStorageDirectory().getPath() + "/scorelog.txt";

	public	static	File 				logFile = null;
	public	static	DataOutputStream	logOutputStream = null;

	public static void init()
	{
		try
		{
			logFile = new File(strLogFileName);

			if (!logFile.exists())
				logFile.createNewFile();

			OutputStream in = new FileOutputStream(logFile);
			logOutputStream = new DataOutputStream(in);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void release()
	{
		try
		{
			logOutputStream.close();
			logOutputStream = null;
			logFile.exists();
			logFile = null;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void log(String strLog)
	{
		if (logOutputStream == null)
			return;

		try
		{
			//logOutputStream.writeUTF(strLog + "\n\n");
			logOutputStream.writeBytes(strLog);
			//logOutputStream.writeBytes("A");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void logBytes(byte[] strLog,int length)
	{
		if (logOutputStream == null)
			return;

		try
		{
			//logOutputStream.writeUTF(strLog + "\n\n");
			logOutputStream.write(strLog, 0, length);
			//logOutputStream.writeBytes("A");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
