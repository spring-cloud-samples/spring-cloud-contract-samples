import java.nio.ByteBuffer

String path = args[0]
println("Reading file [${path}]")
byte[] bytes = new File(path).bytes
println("Current bytes size ${bytes.size()}")

byte compressedFlag = 0;
byte[] message = bytes
byte[] messageLength = ByteBuffer.allocate(4).putInt(message.length).array()
byte[] bodyArray = ByteBuffer.allocate(1 + messageLength.length + message.length).put(compressedFlag).put(messageLength).put(message).array();

new File(path).bytes = bodyArray
println("Stored file [${path}] with new bytes size ${new File(path).size()}")
