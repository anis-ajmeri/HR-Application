package com.shahrukh.mansuri.network.client;
import com.shahrukh.mansuri.network.common.*;
import com.shahrukh.mansuri.network.common.exceptions.*;
import java.io.*;
import java.net.*;
public class NetworkClient 
{
public Response send(Request request) throws NetworkException
{
try
{
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(request);
oos.flush();
byte objectBytes[];
objectBytes=baos.toByteArray();
int requestLength=objectBytes.length;
byte header[]=new byte[1024];
int x;
int i;
i=1023;
x=requestLength;
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
Socket socket=new Socket(Configuration.getHost(),Configuration.getPort());
OutputStream os=socket.getOutputStream();
os.write(header,0,1024); // from which index, how many 
os.flush();
InputStream is=socket.getInputStream();
byte ack[]=new byte[1];
int bytesReadCount;
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
int bytesToSend=requestLength;
int chunkSize=1024;
int j=0;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize) chunkSize=bytesToSend-j;
os.write(objectBytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
int bytesToReceive=1024;
byte tmp[]=new byte[1024];
int k;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
header[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
int responseLength=0;
i=1;
j=1023;
while(j>=0)
{
responseLength=responseLength+(header[j]*i);
i=i*10;
j--;
}
ack[0]=1;
os.write(ack,0,1);
os.flush();
byte response[]=new byte[responseLength];
bytesToReceive=responseLength;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
response[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
ack[0]=1;
os.write(ack);
os.flush();
socket.close();
ByteArrayInputStream bais=new ByteArrayInputStream(response);
ObjectInputStream ois=new ObjectInputStream(bais);
Response responseObject=(Response)ois.readObject();
return responseObject;
}catch(Exception e)
{
throw new NetworkException(e.getMessage());
}
}
}