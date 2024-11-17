Name: Shiyu Liu        NetID: sliu89
email: sliu89@u.rochester.edu

This project uses Huffman compression to encode and decode a file. At first,
I created a Node class and a MinPQ class. The Node class is generates a special 
node for Huffman tree. Every node stores a char, the frequency of this char and 
references to its left and right children. The MinPQ is a modification of PriorityQueue
class: it stores keys in a decreasing order from root to leafs. The main different between
a MaxPQ and a MinPQ is instead of less(), I implemented a more() method. 

In HuffmanSubmit.java, the two major tasks are encode and decode. The first step 
for encode() is to read input from the inputfile. I used readChar() of BinaryIn and 
made a char array out of all the characters from the inputfile. Then, I used an array
called freq[] to count the frequency of each character. With freq[], I was able to build 
a Huffman tree using the buildTrie() method. The buildTrier() method takesin an array
 of int to create a MinPQ of Nodes. At first, it inserts a Node for every char in freq[] 
into the trie. Then, it combines two nodes with smallest freq into a parent node over 
and over in a while loop. After the Huffman trie is constructed,  I created a lookup table
dictionary[] using buildDic(). It traverses down the trie and stores a bitstring for each 
char in the leafs. Now it's time for wirting the encoded file. First of all, I used encodeTrie()
to encode the Huffman trie recursively onto the outputfile. Then, I print the total number of 
characters of the inputfile for future usage. Next, I started to encode every character from
the inputfile by looking up its substring in dictionary[]. After I've encoded every character,
I used writeDic() to traverse the Huffman tree and record the frequency of each character onto
the freqfile. 

Finally, we enter the decode method. First of all, I decoded the Huffman trie from the 
encoded file. Secondly, I read the total number of bytes and contructed a for loop to
decode the inputfile. I used the Huffman trie to find the node containing each character
and then prints it out onto a new file. In this way, the file can be decoded.

To encode or decode a file,  please copy and paste the file into the same package containing
HuffmanSubmit.java. The decoded file can be found in the java workspace.