<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Customer details and Account details</title>
    <link rel="stylesheet" href="css/Styles.css">  
</head>
<body  bgcolor="add8e6" class="center">
                <center>
                <header >
                                <img align="left" class="image" src="img/citi.png" alt="logo" width="100" height="100" />
                                <h1 align ="center" style="color:white;"><i>CITI BANK</i></h1></header></center>
                               
                        

                             <h1 class="top-left" style="color:blue;"><i>AddNewAccount</i></h1>
                
     <form action="createAccount.mm">    
     <article>   
        AccountHolderName:<br>
       <!-- <input type="text" name="First_Name" required><br>!-->
        <input type="text" name="Acct_Hname" required>
        <br>
      
         Salary:<br>
        <input type="number" name="sal" required>
        <br>
        <br>
        Salaried?<br>
         <input type="radio" name="saltype" value="y" > Yes<br>
  		 <input type="radio" name="saltype" value="n"> No<br>
              <br>
              <br>
             
        <input type="submit"  value="Submit"> &nbsp; &nbsp;
        <input type="reset"  value="Reset"> 
        </article>
     </form>
     
<footer>
                
                <p>Copyright © 2018 Citigroup Inc.</p>
        
 </footer>
</body>
</html>