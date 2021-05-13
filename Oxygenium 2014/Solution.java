import java.util.ArrayList;

public class Solution {

   private ArrayList<Coord> pairs; 
   private final static int N=1000000000;
   private int lenArray; 
    
   private int countStreaks(){
        long result=0;
        long left;
        long right;     
        long current_end=-1;        
        for (Coord e: pairs){    
            left=e.getWest();
            right=e.getEast();
            if (left==right){
                result++;
            } else{
                if (left<current_end)
                    result+=(current_end-left+1)*
                            (right-current_end);
                else 
                    if (left==current_end)
                       result+=(right-left); 
                left=current_end+1;
                result+=(right-left+2)*(right-left+1)/2;
            }
            current_end=right;   
        }
        return (int) ((result>N)?N:result);
    }

 
    
    private void insertCut(int K, int[] B){        
        pairs=new ArrayList<Coord>();
        lenArray=B.length;
        int step=1;
        double[] A=new double[lenArray];
        for (int i=0; i<lenArray; i++)
                A[i]=(double) B[i];
        int[] UnderHead=new int[lenArray];
        for(int i=0; i<lenArray; i++)
            UnderHead[i]=0;
        int hP=-1; int hN=-1;
        for (int i=0; i<lenArray-1-K; i++){
            if(A[i+K+1]==A[i]+K+1){
                if (hN>0)
                    hN=-1;
                if(hP<0)
                    hP=i+K+1;
                if(hP>=0 && i+2==hP){
                    for(int j=hP; j<hP+K; j++)
                        UnderHead[j]=1;
                    hP=-1;
                }    
            }else{
               if (A[i+K+1]+K+1==A[i]){
                   if (hP>0)
                       hP=-1;
                   if(hN<0) 
                       hN=i+K+1;
                   if(hN>=0 && i+2==hN){
                     for(int j=hN; j<hN+K; j++)
                        UnderHead[j]=-1;
                     hN=-1;
                   }    
               } else{
                   UnderHead[i]=0;
                   hP=-1;
                   hN=-1;
               }       
            }
         }
         if(hP>=0 && hP<lenArray)
             for (int j=hP; j<lenArray; j++)
               UnderHead[j]=1;
         if(hN>=0 && hN<lenArray)
             for (int j=hN; j<lenArray; j++)
               UnderHead[j]=-1;  
        Coord margin=new Coord(0,-1);
        Coord previous=new Coord(0,0);
        Coord extreme=new Coord(previous);
        for (int i=1; i<lenArray; i++){
             if(A[i]<A[extreme.getWest()]) 
                {
                 previous.setWest(extreme.getWest()); 
                 extreme.setWest(i);
               }
             else{
                if(A[i]>A[extreme.getEast()]) 
                    {
                     previous.setEast(extreme.getEast()); 
                     extreme.setEast(i);
                    }
             } 
             if (A[extreme.getEast()]-A[extreme.getWest()]>K){
                margin.setEast(i-1);
                Coord temp=new Coord(margin);
                pairs.add(temp);
                if (UnderHead[i]==0){
                   int j=i-1;
                   if(A[i]-A[j]>0){
                   previous.setEast(extreme.getEast());
                   extreme.setWest(i);
                   while (A[i]-A[j]<=K && j>previous.getWest()){
                       if (A[j]<A[extreme.getWest()]) extreme.setWest(j);
                       j--;
                   }
                   previous.setWest(extreme.getWest()); 
                 }else{
                    previous.setWest(extreme.getWest()); 
                    extreme.setEast(i);                   
                    while (A[j]-A[i]<=K && j>previous.getEast()){
                        if (A[j]>A[extreme.getEast()]) extreme.setEast(j);
                        j--;
                    }
                    previous.setEast(extreme.getEast());
                  }
                  margin.setWest(j+1);  
             }
             else{
                 int tempUH=i-K;
                 margin.setWest(tempUH);
                 if(UnderHead[i]==1){
                     extreme.setWest(tempUH);
                     previous.setWest(tempUH);}
                     else{
                        extreme.setEast(tempUH);
                        previous.setEast(tempUH);
                 }
             }
             }    
        }      
        margin.setEast(lenArray-1);
        Coord temp=new Coord(margin);
        pairs.add(temp);                 
      }
   
    
    
    
    public int solution(int K, int[] A){
        insertCut(K, A);  
         
        return countStreaks();
    }
    
    
    private class Coord{
        private int West;
        private int East;
        
        public int getWest(){
            return West;
        }
        
        public int getEast(){
            return East;
        }
        
        public Coord(int w, int e){
            West=w;
            East=e;
        }
        
        public Coord(Coord t){
            East=t.getEast();
            West=t.getWest();
        }
        public void setEast(int e){
            East=e;
        }
        
        public void setWest(int w){
            West=w;
        }
}
}    