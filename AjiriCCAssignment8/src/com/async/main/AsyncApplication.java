package com.async.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class AsyncApplication 

{

	public static void main(String[] args) 
	
	{
		ConcurrentMap<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>();
		List<CompletableFuture<List<Integer>>> completableFuturesList = new ArrayList<CompletableFuture<List<Integer>>>();
		Assignment8 assignment = new Assignment8();
		ExecutorService executor = Executors.newCachedThreadPool();
		   
	    for (int i = 0; i < 1000; i++) 
	    {
	    	CompletableFuture<List<Integer>> future = CompletableFuture.supplyAsync(() -> {
	    		return assignment.getNumbers();
	    	}, executor)
	    			.thenApplyAsync((numbers) -> {
	    		return numbers;
	    	}, executor);
	    	completableFuturesList.add(future);
	    }
	    
	    while (completableFuturesList.stream().filter(CompletableFuture::isDone).count() < 1000)
	    {
	    	//wait
	    }
	    
	    //stream to hashmap
	    for (CompletableFuture<List<Integer>> future : completableFuturesList)
	    {
	    	try 
	    	{
				List<Integer> list = future.get();
				for (Integer integer : list)
				{
					if (map.containsKey(integer))
					{
						map.put(integer, map.get(integer) + 1);
					}
					else
					{
						map.put(integer, 1);
					}
				}
			} 
	    	
	    	catch (InterruptedException e) 
	    	{
				e.printStackTrace();
			} 
	    	catch (ExecutionException e) 
	    	{
				e.printStackTrace();
			}
	    }
	    
	    int count = 0;
	    for (ConcurrentMap.Entry<Integer, Integer> entry : map.entrySet())
	    {
	    	count++;
	    	if (count < map.size())
	    	{
	    		System.out.print(entry.getKey() + "=" + entry.getValue() + ", ");
	    	}
	    	else
	    	{
	    		System.out.print(entry.getKey() + "=" + entry.getValue());
	    	}
	    }
	}

}
