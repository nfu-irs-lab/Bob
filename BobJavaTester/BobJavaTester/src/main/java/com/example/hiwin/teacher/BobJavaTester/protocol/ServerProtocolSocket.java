package com.example.hiwin.teacher.BobJavaTester.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Executors;

import com.example.hiwin.teacher.BobJavaTester.protocol.ProtocolSocket.ConnecttionStatus;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ClientHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.Package;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.PackageHeader;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ProtocolListener;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.ServerHelloPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.VerifyResponsePackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.VerifyResponsePackage.Verify;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.SplitDataPackage;

public class ServerProtocolSocket extends ProtocolSocket{
	
}
