
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("unchecked")
public class Solution {

    private final int LMAX=300000;
    private final int NMAX=1000000000;
    private final char DUMMY='X';
    private final int EXTRA=13;
    private char[] charArray;   
    private int sirLen;
    private long product;
    private int root;
    private int limit;
    private int proot;
    
    
     private void findCyclicityLimit(int i, int limitSup){
        int startCompare=i;
        if (limitSup%i==0){
            boolean correct=true;
            int rods=0;
            while (correct && startCompare<=(limitSup-i)){
               for (int k=0; k<i; k++){
                    int next=startCompare+k;
                    if (charArray[k]!=charArray[next]){
                        if (next>limit && rods>0) 
                            {
                               limit=next-1;
                               proot=i;
                            }
                        correct=false;
                        break;
                    }
                }
                rods++;
                startCompare+=i;
            }
            if (correct){
                limit=sirLen;
                root=i;
                proot=i;
            }   
        }        
    }
     
    private void Cyclicity(){
        int i=1;
        limit=0;
        while(i<=Math.floor(sirLen/2)) {  
            if (test_cyclical(i)) {
                limit=sirLen;
                root=i;
                return;
            }
            else i++;
        }    
        proot=0;
        i=1;
        while(proot==0 && i<=Math.floor(sirLen/2) ){
            int limitSup=sirLen;
            if (i<=EXTRA+1){//complete until 13
                int rest=sirLen%i;
                if (rest>0)
                    limitSup=sirLen+i-rest;
            }
            findCyclicityLimit(i, limitSup);  
            i++;
        }
    } 
    
    private boolean test_cyclical(int i){
        boolean b=false;  
        int j=i;
        if (sirLen%i==0){
            boolean correct=true;
            while (correct && j<=(sirLen-i)){
                for (int k=0; k<i; k++){
                    if (charArray[k]!=charArray[j+k]){
                        correct=false;
                        break;
                    }
                }
                j+=i;
            }
            b=correct;
        }
        return b;
    }
    
    
    
      public int solution(String S){
        sirLen=S.length();
        for (int i=0; i<EXTRA; i++)
            S+=DUMMY;
        limit=0;
        charArray=S.toCharArray();
        product=sirLen;
        root=0;
        Cyclicity();
        if (limit<sirLen)
            if (proot==1){
                NonRegular1Product n1=new NonRegular1Product();
                n1.doGoodList();
                n1.discoverL();
            }else{
                Product p=new Product();
                p.doGoodList();    
                p.discoverL();
             }
        else{
            RegularProduct rp=new RegularProduct();  
            rp.oneWord();
        }
        return (int) product; 
    }
    
      
    class NonRegular1Product extends Product{
        private final LinkedList<Integer> left, right;
        private int diffInterval;
        private boolean isLastInterval;
        
        NonRegular1Product(){
            left=new LinkedList<Integer>();
            right=new LinkedList<Integer>();
            int i=0;
            int aLeft=0;
            int aRight=limit;
            left.add(i, aLeft);
            right.add(i,aRight);
            int j=limit+1;
            boolean complete=true;
            while (j<charArray.length){
                if (!complete){
                    if (charArray[j]!=charArray[0]){
                        aRight=j-1;
                        complete=true;
                        i++;
                        left.add(i, aLeft);
                        right.add(i,aRight);
                    }
                }    
                else{
                    if (charArray[j]==charArray[0]){
                        aLeft=j;
                        complete=false;
                    }
                }    
                j++;
            }
            if (!complete){
                aRight=j;
                i++;
                left.add(i, aLeft);
                right.add(i,aRight);
            }
        }
        
        
        private int checkInList(int key){
            int k=0;
            for(int i=0; i<left.size(); i++){
                if (key>=left.get(i) && key<=right.get(i)){
                    k=right.get(i);
                    diffInterval=right.get(i)-left.get(i);
                    isLastInterval=(i==left.size()-1);
                    break;
                }       
            }
            return k;
        }
        
        
            @Override
       public void discoverL() {
          tm=new TreeMap<Integer, Integer>(Collections.reverseOrder());
          Iterator<Integer> dlRevIter=goodIndex.iterator();
          Iterator<Integer> dlIter=goodIndex.descendingIterator();  
          tm.put(new Integer(sirLen), new Integer(1));
          change=0;
          int tmp=dlRevIter.next();
          while (dlIter.hasNext() && dlRevIter.hasNext()){ 
               int kN=dlIter.next();
               int kRev=dlRevIter.next();
               if (kRev>kN) break;
               Add1Item(kN);     
               if (kRev==kN) break;
               Add1Item(kRev);
               if (change%(LMAX/50)==0)
                   checkProduct();
         }
         checkProduct();       
    }
        
        @Override
       protected void Add1Item(int key){
            int lS=checkInList(key);
            if (limit>diffInterval)  
                 enhancedAdd(lS-key+1);
            else
                    if(lS-key>limit)
                       enhancedAdd(limit+1);
                    else                      
                      if(lS-key==limit)
                        if (isLastInterval)
                          enhancedAdd(limit+1);
                        else
                           addItem(key);
                      else
                          enhancedAdd(lS-key+1);           
       }
       
        @Override
        protected void enhancedAdd(int key){
            addInMap(key);
        }
    }
      
    class RegularProduct {
        
         private void oneWord(){
         int change=0;
         int k=sirLen;
         while (k>0){
              change++;
              int np=k*change;
              if (np>product) product=np;
              if (product>=NMAX){
                          product=NMAX;
                          return;
              }
              k-=root;
         }
    }     
    }
      
      
      class Product implements ComputeProduct{
        
        TreeMap<Integer, Integer> tm;
        int change;
        LinkedList<Integer> goodIndex;
    
        
        
        @Override
       public void discoverL() {
          tm=new TreeMap<Integer, Integer>(Collections.reverseOrder());
          Iterator<Integer> dlRevIter=goodIndex.iterator();
          Iterator<Integer> dlIter=goodIndex.descendingIterator();  
          tm.put(new Integer(sirLen), new Integer(1));
          change=0;
          int tmp=dlRevIter.next();
          while (dlIter.hasNext() && dlRevIter.hasNext()){ 
               int k=dlIter.next();
               int kRev=dlRevIter.next();
               if (kRev>k) break;
               Add1Item(k);    
               if (kRev==k) break;
               Add1Item(kRev);

               if (change%(LMAX/50)==0)
                   checkProduct();
         }
         checkProduct();       
    }

       protected void Add1Item(int key){
               if(key<limit)
                   enhancedAdd(key);     
               else    
                   addItem(key);
       }
       
 
        @Override
        public void doGoodList() {
            goodIndex=new LinkedList();
        if (limit==sirLen) return;
        char first=charArray[0];
        if (sirLen>1){
            char second=charArray[1];
            int i=0; 
            while(i<sirLen-1){
                char tmp1=charArray[i];
                char tmp2=charArray[i+1];
                if (tmp1==first && tmp2==second){
                    goodIndex.add(i);
                }
                i++;
            }
            int a=goodIndex.size()*2;
            product=(a>product)?a:product; 
        }    
        else
            goodIndex.add(0);
    }        
        
   protected void checkProduct(){
          int curr_sNr=0;
          for ( Map.Entry<Integer, Integer> entry: tm.entrySet()) {
          
              curr_sNr+=entry.getValue();
              int np=curr_sNr*entry.getKey();
              if (np>product) product=np;
              if (product>=NMAX){
                     product=NMAX;
                     return;
               } 
          } 
    }

 protected void addItem(int key){
       int r=2;
       boolean diff=false;
       int j=key+r;
       while (j<sirLen && !diff ){
            if (charArray[r]==charArray[j]){
                 r++;
                 j++;
            }        
            else
                diff=true;
        }
        addInMap(r);
    }
 
 protected void addInMap(int r){
     if (r>2){
                   change++;
                   Integer v=tm.get(r);
                   tm.put(r, (v==null)?1:v+1);
               }
  } 
   
    
  protected void enhancedAdd(int key){
       int r=2;
       boolean diff=false;
       int j=key+r;
       if (key%proot>0)
            while (j<sirLen && !diff ){
                 if (charArray[r]==charArray[j]){
                     r++;
                     j++;
                 }    
                 else
                     diff=true;
            }
        else
           r=limit+1-key;
        addInMap(r);
    }
}
    
    
    
interface ComputeProduct{
    void discoverL();
    void doGoodList();
    }   
}
