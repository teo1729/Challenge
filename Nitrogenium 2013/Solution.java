import java.util.Iterator;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;


public class Solution {
    
    public int[] solution (int[] A, int[] B){
        HeightsLevels hl=new HeightsLevels(A,B);
        hl.fillResult();
        return hl.getResult();     
    }
        
    
    class HeightsLevels {
        private final TreeMap<Integer, LinkedList<Integer>> heights;
        private final TreeMap<Integer, LinkedList<Integer>> levels;
        private int currentNIslands;
       // private TreeSet<Integer> sl;
        private final int[] result;
        private TreeSet<Integer> set;
        private final int nPoints;
   
        
        public HeightsLevels(int[] A, int[] B) {
            heights=createTreeMap(A);
            nPoints=A.length;
            levels=createTreeMap(B);
            result=new int[B.length];
            currentNIslands=0;
        }

        public int[] getResult(){
            return result;            
        } 
        
        
        public final TreeMap<Integer, LinkedList<Integer>> createTreeMap(int[] par) {
            TreeMap<Integer, LinkedList<Integer>>tm=new TreeMap<Integer, LinkedList<Integer>>();
           // LinkedList<Integer> lElement;   
            for (int i=0; i<par.length; i++){
                LinkedList<Integer> v=tm.get(par[i]);
                if(v==null)
                    v=new LinkedList<Integer>();
                v.add(i);
                tm.put(par[i], v);               
             }            
             return tm;
          }

              
        private void addSet(int hCurrent){
            LinkedList<Integer> element=heights.get(hCurrent);
            Iterator<Integer> iter=element.iterator();
            while(iter.hasNext()){
                int tmp=iter.next();
                set.add(tmp);
                boolean btm1=set.contains(tmp+1);
                boolean btm2=set.contains(tmp-1);
                if (tmp>0 && tmp<nPoints-1){
                    if (btm1 && btm2) currentNIslands--;
                    if (!btm1 && !btm2) currentNIslands++;
                }else if (tmp==0){
                            if(!btm1) currentNIslands++;
                         }
                      else
                            if(!btm2) currentNIslands++;                
            } 
        }

        private void setResult(int lCurrent){
           LinkedList<Integer> element=levels.get(lCurrent);
           Iterator<Integer> iter=element.iterator();
           while(iter.hasNext()){                
               result[iter.next()]=currentNIslands;
           } 
        }
      
        
        private void fillResult() {
            NavigableSet<Integer> lset=levels.navigableKeySet();
            set=new TreeSet<Integer>();
            NavigableSet<Integer> hset=heights.navigableKeySet();      
            Iterator<Integer> lIter=lset.descendingIterator();
            Iterator<Integer> hIter=hset.descendingIterator();
            if (hIter.hasNext()){
                int hCurrent=hIter.next();
                while (lIter.hasNext() ){
                    int lCurrent=lIter.next();            
                    while (hCurrent>lCurrent ){
                        addSet(hCurrent);
                        if (!hIter.hasNext()) break;
                        hCurrent=hIter.next();    
                    }
                  //  System.out.println("set:"+set);
                    setResult(lCurrent);                    
               }
            }     
        }
    }
}
