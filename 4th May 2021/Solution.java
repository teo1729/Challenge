import java.util.HashMap;
import java.util.Map;

class Solution {
    public int solution(int[] A, int L, int R) {
       HashMap<Integer, Integer> elem =new HashMap<>();
       int result=0;
       for (int x:A) {
    	 if(elem.containsKey(Integer.valueOf(x))) {    		 
    		 if (elem.get(x)==1) 
    			 elem.put(x, 2);
    	 } else {
    		 elem.put(x, 1);
    	 }
       }              
       for (Map.Entry<Integer, Integer> entry : elem.entrySet()) {
           if (entry.getKey()<L || entry.getKey()>R) {
        	   if (entry.getKey()<L && entry.getKey()>R)
        		   result+=entry.getValue();
        	   else 
        		   result++;
           }           
       }       
       return result;
    }
}