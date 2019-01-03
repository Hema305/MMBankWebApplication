<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>UpdateCustomerForm</title>
    <link rel="stylesheet" href="css/Styles.css">  
</head>
<body  bgcolor="add8e6" class="center">
                <center>
                <header>
                                <img align="left" class="image" src="img/citi.png" alt="logo" width="100" height="100" />
                                <h1 align ="center" style="color:white;"><i>CITI BANK</i></h1></header></center>
                               
                        
                 </header>
            
                <h1 class="top-left" style="color:blue"><i>Update your details here</i></h1>
        <form action="updateAccount.mm">    
     <article>   
       Account Number:<br>
       <!-- <input type="text" name="First_Name" required><br>!-->
        <input type="text" name="acctNum" value="${requestScope.accounts.bankAccount.accountNumber}" readonly>
        <br>
        AccountHolderName:<br>
       <!-- <input type="text" name="First_Name" required><br>!-->
        <input type="text" name="Acct_Hname" value="${requestScope.accounts.bankAccount.accountHolderName}" required placeholder="you can modify your name here">
        <br>
      
         Salary:<br>
        <input type="number" name="sal" value="${requestScope.accounts.bankAccount.accountBalance}" readonly>
        <br>
        <br>
        Salaried?<br>
         <input type="radio" name="saltype" value=${requestScope.accounts.salary==true?"checked":""}> Yes<br>
  		 <input type="radio" name="saltype" value=${requestScope.accounts.salary==true?"":"checked"}> No<br>
              <br>
              <br>
             
        <input type="submit"  value="Update"> &nbsp; &nbsp;
        <input type="reset"  value="Reset"> 
        </article>
     </form>           
 
    <footer>
                
            <p> <p>Copyright © 2018 Citigroup Inc.</p>
            
     </footer>
</body>
</html>