# Charlene's Coffee Corner
## Author: Przemysław Jesionek, pjesionek@deloitece.com

### Assumptions:
To use stamp card, its number must be provided after making order.  
Card number entered for the first time means its registration.  
Order history is stored for the specified card. 
Order history is stored in memory. To see that effect, select to continue ordering when asked about.  
"every 5th beverage is for free" - ony main part is free - like coffee or juice, without extras.  
"If a customer orders a beverage and a snack, one of the extra's is free." - only one,  
even if there is more than one pair of a drink and a snack, no card needed.  
The cheapest possible item is selected as the discount.  
  
Solution is dynamic, it is easy to expand choice of products, just add them to assortment.properties file.  
Extras have a defined type to which they can be added,  
such approach allows for adding new products with validation logic build in.  

### Import project to IDE, or on Windows:
#### to build (java 1.8+):
> ..\coffee_corner\src>javac -d ..\out\production com\charlenes\coffee_corner\CharlenesCoffeeCorner.java
> ..\coffee_corner\test>javac -d ..\out\test -cp .;..\out\production\;..\lib\junit-jupiter-api-5.8.1.jar;..\lib\apiguardian-api-1.1.2.jar com\charlenes\coffee_corner\*.java com\charlenes\coffee_corner\parser\*.java com\charlenes\coffee_corner\receipt\*.java com\charlenes\coffee_corner\storage\*.java

#### to run program:
> ..\coffee_corner\out\production>java -cp .;..\..\src\resources com.charlenes.coffee_corner.CharlenesCoffeeCorner

#### to run tests:
> ..\coffee_corner\out\test>java -jar ..\..\lib\junit-platform-console-standalone-1.8.2.jar -cp .;..\production;..\..\test\resources --scan-classpath

