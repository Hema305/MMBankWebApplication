package com.capgemini.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.mysql.fabric.Response;

@WebServlet("*.mm")
public class Controller extends HttpServlet {
	boolean flag = false;
	private SavingsAccountServiceImpl savingsAccountService;
	private RequestDispatcher dispatcher;
	private static final long serialVersionUID = 1L;
	@Override
	public void init() throws ServletException {
		super.init();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection
					("jdbc:mysql://localhost:3306/bankapp_db", "root", "root");
			PreparedStatement preparedStatement = 
					connection.prepareStatement("DELETE FROM ACCOUNT");
			preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path=request.getServletPath();
		SavingsAccountService savingsAccountService=new SavingsAccountServiceImpl();
		SavingsAccount savingsAccount=null;
		PrintWriter out = response.getWriter();
		int accountNumber;
		String accountHolderName;
		double amount;
		double salary;
		
		switch(path)
		{
			case "/addNewAccount.mm":
				response.sendRedirect("AddNewAccount.jsp");
				break;
			case "/createAccount.mm":
				accountHolderName=request.getParameter("Acct_Hname");
				salary=Double.parseDouble(request.getParameter("sal"));
				boolean salariedType=request.getParameter("saltype").equalsIgnoreCase("y")?true:false;
				try {
					savingsAccountService.createNewAccount(accountHolderName,salary,salariedType);
					response.sendRedirect("getAll.mm");
					
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "/closeAccount.mm":
				response.sendRedirect("closeAccount.html");
				break;
			case "/accountClose.mm":
				accountNumber=Integer.parseInt(request.getParameter("acctNum"));
				try {
					savingsAccountService.deleteAccount(accountNumber);
					response.sendRedirect("getAll.mm");
					
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "/checkBalance.mm":
				response.sendRedirect("checkBalance.html");
				break;
			case "/accountBalance.mm":
				accountNumber=Integer.parseInt(request.getParameter("acctNum"));
				
					try {
						double currentBalance=savingsAccountService.checkBalance(accountNumber);
						out.print("Current Balance of ("+accountNumber+") account is "+currentBalance);	
						
						
					} catch (ClassNotFoundException | SQLException
							| AccountNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
						
				}
					break;
			case "/deposit.mm":
				response.sendRedirect("DepositForm.html");
				break;
			case "/depositePage.mm":
			
				accountNumber =Integer.parseInt(request.getParameter("AccountNumber"));
				amount = Double.parseDouble(request.getParameter("AmountToDeposit"));
			
				
				
				
				try {
					savingsAccount = savingsAccountService.getAccountById(accountNumber);
					savingsAccountService.deposit(savingsAccount, amount);
					DBUtil.commit();
					response.sendRedirect("getAll.mm");
				
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					try {
						DBUtil.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} catch (Exception e) {
					try {
						DBUtil.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				break;
			case "/withdraw.mm":
				response.sendRedirect("WithdrawForm.html");
				break;
			case "/withdrawPage.mm":
				accountNumber =Integer.parseInt(request.getParameter("AccountNumber"));
				amount = Double.parseDouble(request.getParameter("AmountToDeposit"));
				try {
					savingsAccount = savingsAccountService.getAccountById(accountNumber);
					savingsAccountService.withdraw(savingsAccount, amount);
					DBUtil.commit();
					response.sendRedirect("getAll.mm");
					
				} catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
					try {
						DBUtil.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				} catch (Exception e) {
					try {
						DBUtil.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				break;
			case "/FundTransfer.mm":
				response.sendRedirect("FundTransfer.html");
				break;
			case "/fund.mm":
				
				int senderAccountNumber = Integer.parseInt(request.getParameter("FromAccountNo"));
				
				
				int receiverAccountNumber = Integer.parseInt(request.getParameter("ToAccountNo"));
				
				amount = Double.parseDouble(request.getParameter("AmountToTransfer"));
				try {
					SavingsAccount senderSavingsAccount = savingsAccountService.getAccountById(senderAccountNumber);
					SavingsAccount receiverSavingsAccount = savingsAccountService.getAccountById(receiverAccountNumber);
					savingsAccountService.fundTransfer(senderSavingsAccount, receiverSavingsAccount, amount);
					response.sendRedirect("getAll.mm");
					
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
			case "/getAll.mm":
				try {
					List<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
					request.setAttribute("accounts", accounts);
					dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case "/searchForm.mm":
				response.sendRedirect("searchAccount.jsp");
				break;
			case "/search.mm":
				accountNumber = Integer.parseInt(request.getParameter("AccountNumber"));
				try {
					SavingsAccount account = savingsAccountService.getAccountById(accountNumber);
					request.setAttribute("account", account);
					dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case "/sortByAccountNumber.mm":
				
				try {
					
					flag=!flag;
					ArrayList< SavingsAccount>accounts=new ArrayList<SavingsAccount>();
				/*	Collection<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
					Set<SavingsAccount> accountSet = new TreeSet<>(new Comparator<SavingsAccount>()*/ 
					accounts=(ArrayList<SavingsAccount>)savingsAccountService.getAllSavingsAccount();
					Collections.sort(accounts, new Comparator<SavingsAccount>() 
					{
						@Override
						public int compare(SavingsAccount arg0, SavingsAccount arg1) {
							
							int result= (arg0.getBankAccount().getAccountNumber()-
									(arg1.getBankAccount().getAccountNumber()));
							if(flag==true)
								return result;
							else
								return -result;
								
						}
						
						
					});
					/*accountSet.addAll(accounts);
					request.setAttribute("accounts", accountSet);*/
					request.setAttribute("accounts", accounts);
					dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
					dispatcher.forward(request, response);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
								
				case "/sortByName.mm":
								
								try {
									
									flag=!flag;
									ArrayList< SavingsAccount>accounts=new ArrayList<SavingsAccount>();
								/*	Collection<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
									Set<SavingsAccount> accountSet = new TreeSet<>(new Comparator<SavingsAccount>()*/ 
									accounts=(ArrayList<SavingsAccount>)savingsAccountService.getAllSavingsAccount();
									Collections.sort(accounts, new Comparator<SavingsAccount>() 
									{
										@Override
										public int compare(SavingsAccount arg0, SavingsAccount arg1) {
											
											int result= (arg0.getBankAccount().getAccountHolderName().compareTo
													(arg1.getBankAccount().getAccountHolderName()));
											if(flag==true)
												return result;
											else
												return -result;
												
										}
										
										
									});
									/*accountSet.addAll(accounts);
									request.setAttribute("accounts", accountSet);*/
									request.setAttribute("accounts", accounts);
									dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
									dispatcher.forward(request, response);
								} catch (ClassNotFoundException | SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								break;
								
				case "/sortByBalance.mm":
								
								try {
									
									flag=!flag;
									ArrayList< SavingsAccount>accounts=new ArrayList<SavingsAccount>();
								/*	Collection<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
									Set<SavingsAccount> accountSet = new TreeSet<>(new Comparator<SavingsAccount>()*/ 
									accounts=(ArrayList<SavingsAccount>)savingsAccountService.getAllSavingsAccount();
									Collections.sort(accounts, new Comparator<SavingsAccount>() 
									{
										@Override
										public int compare(SavingsAccount arg0, SavingsAccount arg1) {
											
											int result= (int) (arg0.getBankAccount().getAccountBalance()-
													(arg1.getBankAccount().getAccountBalance()));
											if(flag==true)
												return result;
											else
												return -result;
												
										}
										
										
									});
									/*accountSet.addAll(accounts);
									request.setAttribute("accounts", accountSet);*/
									request.setAttribute("accounts", accounts);
									dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
									dispatcher.forward(request, response);
								} catch (ClassNotFoundException | SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								break;
				case "/sortBySalaryType.mm":
					
					try {
						
						flag=!flag;
						ArrayList< SavingsAccount>accounts=new ArrayList<SavingsAccount>();
					/*	Collection<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
						Set<SavingsAccount> accountSet = new TreeSet<>(new Comparator<SavingsAccount>()*/ 
						accounts=(ArrayList<SavingsAccount>)savingsAccountService.getAllSavingsAccount();
						Collections.sort(accounts, new Comparator<SavingsAccount>() 
						{
						
						
								
							
								@Override
								public int compare(SavingsAccount arg0, SavingsAccount arg1) {
									int result= Boolean.compare(arg0.isSalary(), arg1.isSalary());
									if(flag==true)
										return result;
									else
										return -result;
										
									
								}
								
							
							
							
						});
						/*accountSet.addAll(accounts);
						request.setAttribute("accounts", accountSet);*/
						request.setAttribute("accounts", accounts);
						dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
						dispatcher.forward(request, response);
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				case"/updateForm.mm":
					response.sendRedirect("update.html");
					break;
				case"/update.mm":
						accountNumber = Integer.parseInt(request.getParameter("acctNum"));
				try {
					SavingsAccount accountUpdate = savingsAccountService.getAccountById(accountNumber);
					request.setAttribute("accounts", accountUpdate);
					dispatcher = request.getRequestDispatcher("UpdateDetails.jsp");
					dispatcher.forward(request, response);
				} catch (ClassNotFoundException | SQLException| AccountNotFoundException e) {
					e.printStackTrace();
				}
					break;
				case "/updateAccount.mm":
					accountNumber = Integer.parseInt(request.getParameter("acctNum"));
				SavingsAccount accountUpdate;
				try {
					accountUpdate = savingsAccountService.getAccountById(accountNumber);
					accountHolderName = request.getParameter("Acct_Hname");
					accountUpdate.getBankAccount().setAccountHolderName(accountHolderName);
					salary = Double.parseDouble(request.getParameter("sal"));
					boolean isSalary = request.getParameter("saltype").equalsIgnoreCase("no")?false:true;
					accountUpdate.setSalary(isSalary);
					savingsAccountService.updateAccount(accountUpdate);
					response.sendRedirect("getAll.mm");
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					e.printStackTrace();
				}
					break;
					

					default:
						break;
						}
		
		
			
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
