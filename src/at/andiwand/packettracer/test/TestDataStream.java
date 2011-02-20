package at.andiwand.packettracer.test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;


public class TestDataStream {
	
	public static void main(String[] args) throws Throwable {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		dataOutputStream.writeUTF("hallo welt!!");
		dataOutputStream.flush();
		
		System.out.println(outputStream.toByteArray().length);
		System.out.println(Arrays.toString(outputStream.toByteArray()));
		System.out.println(new String(outputStream.toByteArray()));
	}
	
}