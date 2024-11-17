
package project2;

//author: Shiyu Liu
//netID: sliu89

public class HuffmanSubmit implements Huffman {
	
	public class Node implements Comparable<Node> {
		// implementation of a Node for Huffman tree
		
		char ch;
		int freq;
		final Node left, right;
		
		Node(char ch, int freq, Node left, Node right){
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		
		public boolean isLeaf() {
			return left == null && right == null;
		}
		
		public int compareTo(Node that) {
			return this.freq - that.freq;
		}

	}
	
	public class MinPQ<Key extends Comparable<Key>> {
		//implementation of a priority queue that stores keys in a decreasing order from the root
		
		private Key[] pq;
		private int N = 0;
		
		public MinPQ(int maxN) {
			pq = (Key[])new Comparable[maxN + 1];
		}
		
		public boolean isEmpty() {
			return N == 0;
		}
		
		public int getSize() {
			return N;
		}
		
		public void insert(Key v) {
			pq[++N] = v;
			swim(N);
		}
		
		public Key delMin() {
			Key min  = pq[1];
			exch(1, N--);
			pq[N + 1] = null;
			sink(1);
			return min;
			
		}
		
		public boolean more(int i, int j) {
			return pq[i].compareTo(pq[j]) > 0;
		}
		
		public void exch(int i, int j) {
			Key t = pq[i];
			pq[i] = pq[j];
			pq[j] = t;
		}
		
		public void swim(int k) {
			while(k > 1 && more(k/2, k)) {
				exch(k/2, k);
				k = k/2;
			}
		}
		
		public void sink(int k) {
			while(2 * k <= N) {
				int j = 2 * k;
				if(j < N && more(j, j + 1))  j++;
				if(!more(k, j))  break;
				exch(k, j);
				k = j;
			}
		}

	}

  
	private Node root;
	private String[] dictionary;
	private static final int ALPHABET = 256;//size of the dictionary
	
	public void buildDic() {
		if(root == null)  return;
		dictionary = new String[ALPHABET];
		buildDic(dictionary, root, "");
	}
	
	public void buildDic(String[] st, Node x, String s) {
		//build the dictionary
		
		if(x.isLeaf()) {  st[x.ch] = s; return;}
		buildDic(st, x.left, s + '0');
		buildDic(st, x.right, s + '1');
	}
	
	public void writeDic(Node x, BinaryOut out) {
		//write the frequency file
		
		if(x.isLeaf()) {out.write(Integer.toBinaryString(x.ch)+ ": " + x.freq + "\n"); return;}
		writeDic(x.left, out);
		writeDic(x.right,out);
		
	}
	
	public Node buildTrie(int[] freq) {
		//build the Huffman tree
		
		MinPQ<Node> pq = new MinPQ<Node>(ALPHABET);
		for(char c = 0; c < ALPHABET; c++) {
			if(freq[c] > 0)
				pq.insert(new Node(c, freq[c], null, null));
		}
		
		while(pq.getSize() > 1) {
			Node x = pq.delMin();
			Node y = pq.delMin();
			Node parent = new Node('\0', x.freq + y.freq, x, y);
			pq.insert(parent);
		}
		return pq.delMin();
	}
	
	public void encodeTrie(Node x, BinaryOut out) {
		//write the huffman tree into an encoded file
		
		if(x.isLeaf()) {
			out.write(true);
			out.write(x.ch);
			return;
		}
		out.write(false);
		encodeTrie(x.left, out);
		encodeTrie(x.right, out);
	}
	
	public Node readEnFile(BinaryIn in) {
		//read the encoded file and reconstruct a tree recursively
		
		if(in.readBoolean())
			return new Node(in.readChar(), 0, null, null);
		return new Node('\0', 0, readEnFile(in), readEnFile(in));
	}
	

 
	public void encode(String inputFile, String outputFile, String freqFile){
		
		 BinaryIn in = new BinaryIn(inputFile);
		 BinaryOut out = new BinaryOut(outputFile);
		 BinaryOut FQout = new BinaryOut(freqFile);
		 int freq[] = new int[ALPHABET];
		 String article = "";
		 
		 while(!in.isEmpty()) {//read the inputFile as chars 
			 article += in.readChar();	 
		 }
		 char[] input = article.toCharArray();
		 for(int i = 0; i < input.length; i++)//count the frequency of each character 
			 freq[input[i]]++;
		 
		 root = buildTrie(freq);//build Huffman tree using freq[] 
		 buildDic();//build dictionary recursively using root
		 
		 encodeTrie(root, out);//encode Huffman tree into bitstring 
		 out.write(input.length);//print the number of bytes in the original file
		 
		 
		 for(int i = 0; i < input.length; i++) {//encode the original inputfile
			 String binaryCode = dictionary[input[i]];
			 for(int j = 0; j < binaryCode.length(); j++) {
				 if(binaryCode.charAt(j) == '1')
					 out.write(true);
				 else  out.write(false);
			 }
		 }
		 out.flush();
		 out.close();
		 
		 writeDic(root, FQout);//write the freqfile recursively
		 FQout.flush();
		 FQout.close();
		 
		 
   }


   public void decode(String inputFile, String outputFile, String freqFile){
	     BinaryIn in = new BinaryIn(inputFile);
		 BinaryOut out = new BinaryOut(outputFile);
		 
		 Node newRoot = readEnFile(in);//decode Huffman trie from the encoded file 
			int N = in.readInt();//read the number of bytes in the original file
			for(int i = 0; i < N; i++) {//decode each bitstring into character using the new trie
				Node x = newRoot;
				while(!x.isLeaf())
					if(in.readBoolean())
						x = x.right;
					else x = x.left;
				out.write(x.ch);
			}
			out.flush();
			out.close();
			
			
			
   }




   public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();
      huffman.encode("ur.jpg", "ur.enc", "freq1.txt");
      huffman.decode("ur.enc", "ur_dec.jpg", "freq1.txt");
      huffman.encode("alice30.txt", "alice30.enc", "freq2.txt");
      huffman.decode("alice30.enc", "alice30_dec.txt", "freq2.txt");
      
      
		
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}

//Work cited:
//Sedgewick, Robert. Wayne, Kevin. "Algorithms Fourth Edition". 5.5 Data Compression P826~838.
//2011 Pearson Education, Inc.

