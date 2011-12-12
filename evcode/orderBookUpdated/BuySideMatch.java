package orderBookUpdated15;

import java.util.PriorityQueue;

public class BuySideMatch
{
	private PriorityQueue<Order> buySideOrder = new PriorityQueue<Order>();
	private PriorityQueue<Order> sellSideOrder = new PriorityQueue<Order>();
	private PriorityQueue<Order> processedOrder = new PriorityQueue<Order>();
	
	public BuySideMatch()
	{
		
	}
	
	public BuySideMatch(PriorityQueue<Order> buySideOrder, PriorityQueue<Order> sellSideOrder)
	{
		this.buySideOrder = buySideOrder;
		this.sellSideOrder = sellSideOrder;
	}
	
	public void setBQ(PriorityQueue<Order> buySideOrder)
	{
		this.buySideOrder = buySideOrder;
	}
	
	public PriorityQueue<Order> getBQ()
	{
		return buySideOrder;
	}
	
	public void setSQ(PriorityQueue<Order> sellSideOrder)
	{
		this.sellSideOrder = sellSideOrder;
	}
	
	public PriorityQueue<Order> getSQ()
	{
		return sellSideOrder;
	}
	
	public PriorityQueue<Order> matchOrder()
	{
		
		 if(buySideOrder.peek().isMarketOrder()&&sellSideOrder.peek() == null)
		 {
			 buySideOrder.peek().setStatus(3);
			 processedOrder.add(buySideOrder.poll());
			 
			 return processedOrder; 
		 }
		 else if (buySideOrder.peek().isMarketOrder()&&sellSideOrder.peek().isLimitOrder())
		 {
			if(buySideOrder.peek().getVolume() == sellSideOrder.peek().getVolume())
			{
				buySideOrder.peek().setStatus(1);
				sellSideOrder.peek().setStatus(1);
				
                buySideOrder.peek().setDealingPrice(sellSideOrder.peek().getPrice());
				
				processedOrder.add(buySideOrder.poll());
				processedOrder.add(sellSideOrder.poll());
				
				return processedOrder;
			}
			else if(buySideOrder.peek().getVolume() < sellSideOrder.peek().getVolume())
			{
				int aVolume = sellSideOrder.peek().getVolume() - buySideOrder.peek().getVolume();
				
				int filledVolume = buySideOrder.peek().getVolume();
				Order filledOrder = new Order();
				filledOrder.setAll(sellSideOrder.peek());
				filledOrder.setStatus(2);
				filledOrder.setVolume(filledVolume);
				processedOrder.add(filledOrder);
				
				sellSideOrder.peek().setVolume(aVolume);
			
				buySideOrder.peek().setStatus(1);
				buySideOrder.peek().setDealingPrice(sellSideOrder.peek().getPrice());
				processedOrder.add(buySideOrder.poll());
				
				return processedOrder;
			}
			else
			{
				double sumBuyPrice = 0;
				int sumBuyVolume = 0;
				//int originalVolume = buySideOrder.peek().getVolume();
				while(buySideOrder.peek().getVolume() > sellSideOrder.peek().getVolume())
				{
					int bVolume = buySideOrder.peek().getVolume() - sellSideOrder.peek().getVolume();
					buySideOrder.peek().setVolume(bVolume);
					sumBuyPrice += (sellSideOrder.peek().getVolume())*(sellSideOrder.peek().getPrice());
					sumBuyVolume += sellSideOrder.peek().getVolume();
					
					sellSideOrder.peek().setStatus(1);
					processedOrder.add(sellSideOrder.poll());
				
					if(sellSideOrder.peek() == null)
					{
						//Order filledOrder = new Order();
						//filledOrder.setAll(buySideOrder.peek());
						//filledOrder.setStatus(3);
						//filledOrder.setVolume(originalVolume - sumBuyVolume);
						//processedOrder.add(filledOrder);
						
						buySideOrder.peek().setStatus(2);
						buySideOrder.peek().setVolume(sumBuyVolume);
						buySideOrder.peek().setDealingPrice(sumBuyPrice/sumBuyVolume);
						processedOrder.add(buySideOrder.poll());
						
						break;
					}
					else
					{
						if(buySideOrder.peek().getVolume() < sellSideOrder.peek().getVolume())
						{
							int cVolume = sellSideOrder.peek().getVolume() - (buySideOrder.peek().getVolume());
							
							int filledVolume = buySideOrder.peek().getVolume();
							Order filledOrder = new Order();
							filledOrder.setAll(sellSideOrder.peek());
							filledOrder.setStatus(2);
							filledOrder.setVolume(filledVolume);
				            processedOrder.add(filledOrder);
							
							sellSideOrder.peek().setVolume(cVolume);
							
							double allPrice = sumBuyPrice+ (sellSideOrder.peek().getPrice())*(buySideOrder.peek().getVolume());
							int allVolume = sumBuyVolume + (buySideOrder.peek().getVolume());
							double averagePrice = (allPrice)/(allVolume);
							
							buySideOrder.peek().setStatus(1);
							buySideOrder.peek().setDealingPrice(averagePrice);
							buySideOrder.peek().setVolume(allVolume);
							processedOrder.add(buySideOrder.poll());
							
							break;
						}
						else if(buySideOrder.peek().getVolume() == sellSideOrder.peek().getVolume())
						{
							double allPrice = sumBuyPrice + (sellSideOrder.peek().getPrice())*(buySideOrder.peek().getVolume());
							int allVolume = sumBuyVolume + (buySideOrder.peek().getVolume());
							double averagePrice = (allPrice)/(allVolume);
							
							sellSideOrder.peek().setStatus(1);
							buySideOrder.peek().setStatus(1);
							buySideOrder.peek().setDealingPrice(averagePrice);
							buySideOrder.peek().setVolume(allVolume);

							processedOrder.add(buySideOrder.poll());
							processedOrder.add(sellSideOrder.poll());
							
							break;
						}
						else
							continue;
					 }
				  }
				return processedOrder;
			   }
		 }
		 else if(((buySideOrder.peek()!=null) &&buySideOrder.peek().isLimitOrder())&&((sellSideOrder.peek()!=null) &&sellSideOrder.peek().isLimitOrder()))
		 {
			 if(buySideOrder.peek().getPrice() == sellSideOrder.peek().getPrice())
			 {
				 if(buySideOrder.peek().getVolume() == sellSideOrder.peek().getVolume())
				 {
					 buySideOrder.peek().setStatus(1);
					 sellSideOrder.peek().setStatus(1);
						
					 processedOrder.add(buySideOrder.poll());
					 processedOrder.add(sellSideOrder.poll());
					 
					 return processedOrder;
				 }
				 else if(buySideOrder.peek().getVolume() < sellSideOrder.peek().getVolume())
				 {
					 int aVolume = sellSideOrder.peek().getVolume() - buySideOrder.peek().getVolume();
					 
					 int filledVolume = buySideOrder.peek().getVolume();
					 Order filledOrder = new Order();
					 filledOrder.setAll(sellSideOrder.peek());
				     filledOrder.setStatus(2);
					 filledOrder.setVolume(filledVolume);
					 processedOrder.add(filledOrder);

					 sellSideOrder.peek().setVolume(aVolume);
					 
					 buySideOrder.peek().setStatus(1);
					 processedOrder.add(buySideOrder.poll());

					 return processedOrder;
				 }
				 else
				 {
					 int sumBuyVolume = 0;
					 
					 while(buySideOrder.peek().getVolume() > sellSideOrder.peek().getVolume())
					 {
						 int bVolume = buySideOrder.peek().getVolume() - sellSideOrder.peek().getVolume();
					     buySideOrder.peek().setVolume(bVolume);
					     
						 sumBuyVolume += sellSideOrder.peek().getVolume();
								
					     sellSideOrder.peek().setStatus(1);
						 processedOrder.add(sellSideOrder.poll());
						
						 if(sellSideOrder.peek() == null)
						 {
							 break;
						 }
							 
						 else
						 {
							 if(buySideOrder.peek().getPrice() != sellSideOrder.peek().getPrice())
							 {
								 break;
							 }
							 else
							 {
								 if(buySideOrder.peek().getVolume() < sellSideOrder.peek().getVolume())
								 {
									 int cVolume = sellSideOrder.peek().getVolume() - (buySideOrder.peek().getVolume());
									 
									 int filledVolume = buySideOrder.peek().getVolume();
									 Order filledOrder = new Order();
									 filledOrder.setAll(sellSideOrder.peek());
									 filledOrder.setStatus(2);
									 filledOrder.setVolume(filledVolume);
									 processedOrder.add(filledOrder);
									 
									 sellSideOrder.peek().setVolume(cVolume);
									 
									 int allVolume = sumBuyVolume + (buySideOrder.peek().getVolume());
									
									 buySideOrder.peek().setStatus(1);
									 buySideOrder.peek().setVolume(allVolume);
									 processedOrder.add(buySideOrder.poll());
									 
									 break;
								 }
								 else if(buySideOrder.peek().getVolume() == sellSideOrder.peek().getVolume())
								 {
									 int allVolume = sumBuyVolume + (buySideOrder.peek().getVolume());
									 
									 buySideOrder.peek().setStatus(1);
									 sellSideOrder.peek().setStatus(1);
									 
									 buySideOrder.peek().setVolume(allVolume);
									 
									 processedOrder.add(buySideOrder.poll());
									 processedOrder.add(sellSideOrder.poll());
									 
									 break;
								 }
								 else
									 continue;
							 }
						 } 
					
					}
					return processedOrder;
				}
			}
			 else
			 {
				 return processedOrder;
			 }
		 }
		 else
		 {
			 return processedOrder;
		 }
	}
}
