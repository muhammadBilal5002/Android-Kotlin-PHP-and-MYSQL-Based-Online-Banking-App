<?php
    include "config/db_config.php";

    $data2 = file_get_contents("php://input");
    $mess = "Hello HOW ARE YOU";
function MYSMSAPI($number,$mystr) {
$new = str_replace(' ', '%20', $mystr);
$url = "http://192.168.43.152:5000/SendSMS/user=&password=123456&phoneNumber=".$number."&msg=".$new."";
$curl = curl_init($url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
curl_setopt($curl, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
$response = curl_exec($curl);
curl_close($curl);
}

    $request = json_decode($data2);

    $response = array();

    $isValidResponse = false;

    if(isset($request -> {"action"})) {
        if($request -> {"action"} == "REGISTER_USER"){
            $isValidResponse = true;
            $userName = $request -> {"userName"};
            $userEmail = $request -> {"userEmail"};
            $userPassword = $request -> {"userPassword"};
            $userPhoneno = $request -> {"userPhoneno"};
            $query = "INSERT INTO user(User_Name, User_Email, User_Password, User_PhoneNo, User_Balance) VALUES('".$userName."','".$userEmail."', '".$userPassword."', '".$userPhoneno."',0)";
            $result = mysqli_query($connection, $query);
            if($result){
                $response["status"] = true;
                $response["responseCode"] = 0;
                $response["message"] = "User Successfully Registered";
            } else{
                $response["status"] = false;
                $response["responseCode"] = 102;
                $response["message"] = "User Registeration Failed";
            }
        }

        else if($request -> {"action"} == "LOGIN_USER"){
            $isValidResponse = true;

            $userEmail = $request -> {"userEmail"};
            $userPassword = $request -> {"userPassword"};
            $query = "SELECT * FROM user WHERE user.User_Email='".$userEmail."' AND user.User_Password= '".$userPassword."'";
            $result = mysqli_query($connection, $query);
            if($result && mysqli_num_rows ($result)>0){
                $row = mysqli_fetch_assoc($result);
                $response["status"] = true;
                $response["respondeCode"] = 0;
                $response["message"] = "User Successfully Login";
                $response["userName"] = $row["User_Name"];
                $response["userEmail"] = $row["User_Email"];
                $response["userBalance"] = $row["User_Balance"];
                $response["userPhoneno"] = $row["User_PhoneNo"];
                $response["userOTP"] = $row["User_OTP"];
            } else{
                $response["status"] = false;
                $response["responseCode"] = 103;
                $response["message"] = "User Login Failed";
            }
        }

        else if($request -> {"action"} == "DO_TRANSACTION"){
            $isValidResponse = true;
            $transactionAmount = $request -> {"transactionAmount"};
            $transactionPurpose = $request -> {"transactionPurpose"};
            $senderEmail = $request -> {"senderEmail"};
            $receiverEmail = $request -> {"receiverEmail"};

            $query1 = "SELECT * FROM user WHERE user.User_Email='".$senderEmail."'";
            $query2 = "SELECT * FROM user WHERE user.User_Email='".$receiverEmail."'";
            
            $checkSenderExists = mysqli_query($connection, $query1);
            $checkReceiverExists = mysqli_query($connection, $query2);

            if(mysqli_num_rows($checkSenderExists)> 0 && mysqli_num_rows($checkReceiverExists)> 0){
                $sender = mysqli_fetch_assoc($checkSenderExists);
                $receiver = mysqli_fetch_assoc($checkReceiverExists);
                $senderBalance = $sender["User_Balance"];
                $receiverBalance = $receiver["User_Balance"];

                if($senderBalance >= $transactionAmount){
                    $updatedSenderBalance = $senderBalance - $transactionAmount;
                    $updatedReceiverBalance = $receiverBalance + $transactionAmount;

                    $query1 = "INSERT INTO transaction(Transaction_Amount, Transaction_Purpose, Sender_Email, Receiver_Email) VALUES('".$transactionAmount."', '".$transactionPurpose."', '".$senderEmail."', '".$receiverEmail."')";
                    $query2 = "UPDATE user SET user.User_Balance='".$updatedSenderBalance."' WHERE user.User_Email='".$senderEmail."'";
                    $query3 = "UPDATE user SET user.User_Balance='".$updatedReceiverBalance."' WHERE user.User_Email='".$receiverEmail."'";
                    
                    $result1 = mysqli_query($connection, $query1);
                    $result2 = mysqli_query($connection, $query2);
                    $result3 = mysqli_query($connection, $query3);
                    if($result1 && $result2 && $result3){
                        $query4 = "SELECT User_Balance FROM user WHERE user.User_Email='".$senderEmail."'";
                        $result4 = mysqli_query($connection, $query4);
                        $row = mysqli_fetch_assoc($result4);
                        MYSMSAPI($sender['User_PhoneNo'],"".$transactionAmount." Transfer Successfully to ".$receiverEmail." Your Current Balance Is ".$updatedSenderBalance."");
                        MYSMSAPI($receiver['User_PhoneNo'],"".$transactionAmount." Has Been Received From ".$senderEmail." Your Current Balance Is ".$updatedReceiverBalance."");
                        $response["status"] = true;
                        $response["responseCode"] = 0;
                        $response["message"] = "Transaction Successfully Done";
                        $response["balance"] = $row['User_Balance'];
                    } 
                } else{
                    $response["status"] = false;
                    $response["responseCode"] = 104;                        
                    $response["message"] = "You Have Insufficient Balance";

                }
            }
            else{
                $response["status"] = false;
                $response["responseCode"] = 104;
                $response["message"] = "Receiver Email Does't Exist";
            }
        }

        else if($request -> {"action"} == "GET_TRANSACTIONS"){
            $isValidResponse = true;

            $userEmail = $request -> {"userEmail"};
            $query = "SELECT * FROM transaction t WHERE t.Sender_Email='".$userEmail."' OR t.Receiver_Email='".$userEmail."' ORDER BY t.Transaction_Date DESC";
            $result = mysqli_query($connection, $query);
            if($result && mysqli_num_rows($result)>0){
                $transactions = array();

                while(($row = mysqli_fetch_assoc($result)) != null){
                    $tran = array();
                    $tran["transactionId"] = $row["Transaction_Id"];
                    $tran["transactionAmount"] = $row["Transaction_Amount"];
                    $tran["transactionDate"] = $row["Transaction_Date"];
                    $tran["transactionPurpose"] = $row["Transaction_Purpose"];
                    $tran["sender"] = $row["Sender_Email"];
                    $tran["receiver"] = $row["Receiver_Email"];

                    $transactions[] = $tran;

                    $response["status"] = true;
                    $response["responseCode"] = 0;
                    $response["message"] = "Transactions Available";
                    $response["transactions"] = $transactions;
                }

            } else{
                $response["status"] = false;
                $response["responseCode"] = 105;
                $response["message"] = "Transactions are not Available";
            }
        }

        else if($request -> {"action"} == "SEND_REQUEST"){
            $isValidResponse = true;

            $requestMessage = $request -> {"requestMessage"};
            $requestAmount = $request -> {"requestAmount"};
            $requesterEmail = $request -> {"requesterEmail"};
            $responderEmail = $request -> {"responderEmail"};

            $query1 = "SELECT * FROM user WHERE user.User_Email='".$requesterEmail."'";
            $query2 = "SELECT * FROM user WHERE user.User_Email='".$responderEmail."'";

            $result1 = mysqli_query($connection, $query1);
            $result2 = mysqli_query($connection, $query2);

            if(mysqli_num_rows($result1)>0 && mysqli_num_rows($result2)>0){
                $query = "INSERT INTO request(Request_Message, Request_Amount, Requester_Email, Responder_Email) VALUES('".$requestMessage."', '".$requestAmount."', '".$requesterEmail."', '".$responderEmail."')";

                $result = mysqli_query($connection, $query);
                if($result){
                    $query4 = "SELECT User_PhoneNo FROM user WHERE user.User_Email='".$responderEmail."'";
                    $result4 = mysqli_query($connection, $query4);
                    $row = mysqli_fetch_assoc($result4);
                    MYSMSAPI($row['User_PhoneNo'],"You Have Reccived Borrow Request From ".$requesterEmail."");
                    $response["status"] = true;
                    $response["responseCode"] = 0;
                    $response["message"] = "Request Successfully Sent";
                    $response["phone"] = $row['User_PhoneNo'];
                }
            } 
            else{
                $response["status"] = false;
                $response["responseCode"] = 106;
                $response["message"] = "Responder Email Does't Exist";
            }
        }

        else if($request -> {"action"} == "GET_REQUESTS"){
            $isValidResponse = true;

            $userEmail = $request -> {"userEmail"};
            $query = "SELECT * FROM request WHERE request.Responder_Email='".$userEmail."' OR request.Requester_Email='".$userEmail."' ";
            $result = mysqli_query($connection, $query);

            if($result && mysqli_num_rows($result)>0){
                $allRequests = array();
                $pendingRequests = array();

                while(($row = mysqli_fetch_assoc($result)) != null){
                    $request = array();
                    $request["requestId"] = $row["Request_Id"];
                    $request["requestMessage"] = $row["Request_Message"];
                    $request["requestAmount"] = $row["Request_Amount"];
                    $request["requestDate"] = $row["Request_Date"];
                    $request["requesterEmail"] = $row["Requester_Email"];
                    $request["requestStatus"] = $row["Request_Status"];

                    if(!$request["requestStatus"]){
                        $pendingRequests[] = $request;
                    }else{
                        
                        $request["respondMessage"] = $row["Respond_Message"];
                        $request["respondAmount"] = $row["Respond_Amount"];
                        $request["respondDate"] = $row["Respond_Date"];
                        $allRequests[] = $request;
                    }
                }
                $response["status"] = true;
                $response["respondeCode"] = 0;
                $response["message"] = "Requests Successfully Received";
                $response["allRequests"] = $allRequests;
                $response["pendingRequests"] = $pendingRequests;
            
            } 
            else{
                $response["status"] = true;
                $response["responseCode"] = 107;
                $response["message"] = "Requests Failed to Receive";
            }

        }

      else if($request -> {"action"} == "RESPOND_REQUEST"){
          $isValidResponse = true;

            $requestId = $request -> {"requestId"};
            $respondMessage = $request -> {"respondMessage"};
            $respondAmount = $request -> {"respondAmount"};

            $query = "SELECT * FROM request INNER JOIN user ON user.User_Email = request.Responder_Email WHERE Request_Id='".$requestId."'";
            $result = mysqli_query($connection, $query);

            if($result && mysqli_num_rows($result)>0){
                $row = mysqli_fetch_assoc($result);
                if(!$row["Request_Status"]){

                    if($row["User_Balance"] >= $respondAmount){
                        $updatedSenderBalance = $row["User_Balance"] - $respondAmount;

                        $query1 = "UPDATE request SET Respond_Message='".$respondMessage."', Respond_Amount='".$respondAmount."', Respond_Date=current_time , Request_Status = 1 WHERE Request_Id='".$requestId."'";
                        $query2 = "UPDATE user SET user.User_Balance='".$updatedSenderBalance."' WHERE user.User_Email ='".$row["Responder_Email"]."'";
                        $query3 = "SELECT * FROM user WHERE user.User_Email='".$row["Requester_Email"]."'";
                        $result1 = mysqli_query($connection, $query1);
                        $result2 = mysqli_query($connection, $query2);
                        $result3 = mysqli_query($connection, $query3);

                        $receiver = mysqli_fetch_assoc($result3);
                        $updatedReceiverBalance = $receiver["User_Balance"] + $respondAmount;

                        $query4 = "UPDATE user SET user.User_Balance='".$updatedReceiverBalance."' WHERE user.User_Email ='".$receiver["User_Email"]."'";
                        $result4 = mysqli_query($connection, $query4);

                        $transactionPurpose = "Request<--->Response\nRequest: ".$row["Request_Message"]." \nResponse: ".$respondMessage."";

                        $query5 = "INSERT INTO transaction(Transaction_Amount, Transaction_Purpose, Sender_Email, Receiver_Email) VALUES('".$respondAmount."', '".$transactionPurpose."', '".$row["Responder_Email"]."', '".$row["Requester_Email"]."')";
                        $result5 = mysqli_query($connection, $query5);

                        if($result1 && $result2 && $result3 && $result4 && $result5){
                            MYSMSAPI($receiver['User_PhoneNo'],"Your Request Has Been Responded By ".$row["Responder_Email"]."");
                            $response["status"] = true;
                            $response["responseCode"] = 0;
                            $response["message"] = "Respond Succesfully Send";
                            $response["balance"] = $updatedSenderBalance;
                        }
                    
                    } else{
                        $response["status"] = false;
                        $response["responseCode"] = 108;
                        $response["message"] = "You Have insufficient Balance";
                    }
                } else{
                    $response["status"] = false;
                    $response["responseCode"] = 109;
                    $response["message"] = "Request Already Responded";
                }
            } else{
                $response["status"] = false;
                $response["responseCode"] = 110;
                $response["message"] = "Respond Failed";
            }
        
      }

       else if($request -> {"action"} == "LOGIN_FM"){
            $isValidResponse = true;

            $userEmail = $request -> {"userEmail"};
            $userPassword = $request -> {"userPassword"};
            $query = "SELECT * FROM franchise_manager WHERE franchise_manager.Franchise_Manager_Email='".$userEmail."' AND franchise_manager.Franchise_Manager_Password= '".$userPassword."'";
            $result = mysqli_query($connection, $query);
            if($result && mysqli_num_rows ($result)>0){
                $row = mysqli_fetch_assoc($result);
                $response["status"] = true;
                $response["respondeCode"] = 0;
                $response["message"] = "User Successfully Login";
                $response["userName"] = $row["Franchise_Manager_Name"];
                $response["userEmail"] = $row["Franchise_Manager_Email"];
                $response["userBalance"] = $row["Franchise_Manager_Balance"];
                $response["userPhoneno"] = $row["Franchise_Manager_PhoneNo"];
                $response["userWithDraw"] = $row["Franchise_Manager_WithDraw"];
                
            } else{
                $response["status"] = false;
                $response["responseCode"] = 111;
                $response["message"] = "User Login Failed";
            }
        }



 else if($request -> {"action"} == "ADD_BALANCE"){
            $isValidResponse = true;

            $transactionAmount = $request -> {"transactionAmount"};
            $transactionPurpose = $request -> {"transactionPurpose"};
            $senderEmail = $request -> {"senderEmail"};
            $receiverEmail = $request -> {"receiverEmail"};

            $query1 = "SELECT * FROM franchise_manager WHERE franchise_manager.Franchise_Manager_Email='".$senderEmail."'";
            $query2 = "SELECT * FROM user WHERE user.User_Email='".$receiverEmail."'";
            
            $checkSenderExists = mysqli_query($connection, $query1);
            $checkReceiverExists = mysqli_query($connection, $query2);

            if(mysqli_num_rows($checkSenderExists)> 0 && mysqli_num_rows($checkReceiverExists)> 0){
                $sender = mysqli_fetch_assoc($checkSenderExists);
                $receiver = mysqli_fetch_assoc($checkReceiverExists);
                $senderBalance = $sender["Franchise_Manager_Balance"];
                $receiverBalance = $receiver["User_Balance"];

                if($senderBalance >= $transactionAmount){
                    $updatedSenderBalance = $senderBalance - $transactionAmount;
                    $updatedReceiverBalance = $receiverBalance + $transactionAmount;
                    $que = "SET foreign_key_checks = 0";
                    $res = mysqli_query($connection, $que);
                    $query1 = "INSERT INTO transaction(Transaction_Amount, Transaction_Purpose, Sender_Email, Receiver_Email) VALUES('".$transactionAmount."', '".$transactionPurpose."', '".$senderEmail."', '".$receiverEmail."')";
                    $query2 = "UPDATE franchise_manager SET franchise_manager.Franchise_Manager_Balance='".$updatedSenderBalance."' WHERE franchise_manager.Franchise_Manager_Email='".$senderEmail."'";
                    $query3 = "UPDATE user SET user.User_Balance='".$updatedReceiverBalance."' WHERE user.User_Email='".$receiverEmail."'";
                    
                    $result1 = mysqli_query($connection, $query1);
                    $result2 = mysqli_query($connection, $query2);
                    $result3 = mysqli_query($connection, $query3);
              
                    if($result1 && $result2 && $result3){
                        MYSMSAPI($sender['Franchise_Manager_PhoneNo'],"".$transactionAmount." Rupees Balance Deducted From Your Account and Added in  ".$receiverEmail." Account Your Current Balance Is ".$updatedSenderBalance."");
                        MYSMSAPI($receiver['User_PhoneNo'],"".$transactionAmount." Rupees Balance Added To Your Account By ".$senderEmail." Your Current Balance Is ".$updatedReceiverBalance."");
                        $response["status"] = true;
                        $response["responseCode"] = 0;
                        $response["message"] = "Transaction Successfully Done";
                        $response["balance"] = $updatedSenderBalance;
                    } 
                } else{
                        $response["status"] = false;
                        $response["responseCode"] = 112;                        
                        $response["message"] = "You Have Insufficient Balance";

                }
            }
            else{
                $response["status"] = false;
                $response["responseCode"] = 113;
                $response["message"] = "Receiver Email Does't Exist";
            }
        }

        else if($request -> {"action"} == "WITHDRAW_REQUEST"){
            $isValidResponse = true;

            $userEmail = $request -> {"userEmail"};
            $fmEmail = $request -> {"fmEmail"};
            $requestAmount = $request -> {"requestAmount"};

            $query1 = "SELECT * FROM user WHERE user.User_Email='".$userEmail."'";
            $query2 = "SELECT * FROM franchise_manager WHERE franchise_manager.Franchise_Manager_Email='".$fmEmail."'";
            $result1 = mysqli_query($connection, $query1);
            $result2 = mysqli_query($connection, $query2);

            if($result1 && $result2 && mysqli_num_rows($result1)>0 && mysqli_num_rows($result2)>0){
                $row1 = mysqli_fetch_assoc($result1);
                $row2 = mysqli_fetch_assoc($result2);
                
                if($row2["Franchise_Manager_WithDraw"] < $requestAmount){
                    $response["status"] = false;
                    $response["responseCode"] = 114;
                    $response["message"] = "Withdraw Amount is not Sufficent";
                }
                else if($row1["User_Balance"] <  $requestAmount){
                    $response["status"] = false;
                    $response["responseCode"] = 115;
                    $response["message"] = "User Balance is not Sufficent";
                }
                else{
                    $otp = rand(100000,999999);
                    $query = "UPDATE user SET USER_OTP='".$otp."' WHERE user.User_Email='".$userEmail."'";
                    $result = mysqli_query($connection, $query);

                    if($result){
                        MYSMSAPI($row1['User_PhoneNo'],"Your OTP CODE: ".$otp."");
                        $response["status"] = true;
                        $response["responseCode"] = 0;
                        $response["message"] = "OTP Has Sent";
                    }
                }
            } else{
                $response["status"] = false;
                $response["responseCode"] = 116;
                $response["message"] = "Withdraw Request Failed";
            }
        }

        else if($request -> {"action"} == "WITHDRAW_RESPONSE"){
            $isValidResponse = true;

            $userEmail = $request -> {"userEmail"};
            $fmEmail = $request -> {"fmEmail"};
            $requestAmount = $request -> {"requestAmount"};
            $userOTP = $request -> {"userOTP"};

            $query1 = "SELECT * FROM user WHERE user.User_Email='".$userEmail."'";
            $query2 = "SELECT * FROM franchise_manager WHERE franchise_manager.Franchise_Manager_Email='".$fmEmail."'";
            $result1 = mysqli_query($connection, $query1);
            $result2 = mysqli_query($connection, $query2);

            if($result1 && $result2 && mysqli_num_rows($result1)>0 && mysqli_num_rows($result2)>0){
                $row1 = mysqli_fetch_assoc($result1);
                $row2 = mysqli_fetch_assoc($result2);
                
                if($row1["User_OTP"] == $userOTP){
                    if($row1["User_Balance"] <  $requestAmount){
                        $response["status"] = false;
                        $response["responseCode"] = 117;
                        $response["message"] = "User Balance is not Sufficent";
                    } else{
                        $userBalance = $row1["User_Balance"];
                        $updatedUserBalance = $userBalance - $requestAmount;
                        $fmWithDraw = $row2["Franchise_Manager_WithDraw"];
                        $updatedfmWithDraw = $fmWithDraw - $requestAmount;

                        $query1 = "UPDATE user SET User_Balance='".$updatedUserBalance."', User_OTP=NULL WHERE user.User_Email='".$userEmail."'" ;
                        $query2 = "UPDATE franchise_manager SET Franchise_Manager_WithDraw='".$updatedfmWithDraw."' WHERE Franchise_Manager_Email='".$fmEmail."'" ;
                        $result1 = mysqli_query($connection, $query1);
                        $result2 = mysqli_query($connection, $query2);

                        if($result1 && $result2){
                            $response["status"] = true;
                            $response["responseCode"] = 0;
                            $response["message"] = "Successfully WithDrawn";
                            $response["withDraw"] = $updatedfmWithDraw;
                        }
                    }
                } else{
                    $response["status"] = false;
                    $response["responseCode"] = 118;
                    $response["message"] = "OTP is Not Correct";
                }
            } else{
                $response["status"] = false;
                $response["responseCode"] = 119;
                $response["message"] = "Withdraw Response Failed";
            }
        }

  
    if(!$isValidResponse){
            $response["status"] = false;
            $response["responseCode"] = 101;
            $response["message"] = "Invalid Action Request";
    }
    } else{
        $response["Status"] = false;
        $response["responseCode"] = 100;
        $response["message"] = "Request action not defined";
    }

    echo json_encode($response);

?>