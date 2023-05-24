# Login API Design Documentation

## Introduction
The login API will offer a token to the calling client. The client will pass in a username and password hash the Login API will then verify that the credentials are indeed correct and then return a valid token to be passed around with requests to the other services. 

## Requirements
Login API will have one endpoint that responds to a POST method. The payload includes username and password.

Firstly, upon successful verification of username and password hash the Login service will query the tbl_user table to locate a row with valid username and then return the `id`, `username`, `password_hash`, `failed_login_attempts`, `deleted`.

- On the first login attempt a token may not be created at this point it will insert into the tbl_user_token table to generate a new token. 
- Within the tbl_user_token table we have an expiry_date if that date is in the past the token is no longer valid and the system will generate a new token and update the tbl_user_token table with a new token and a new expiry_date.

If you send through an incorrect password the service will increment a failed_login_attempts in the tbl_user table and return an error message.

All input/output data will be in JSON format, example below:

## Input: api/login
```
{
	“username”: “will123”,
	“password”:”password123”
}
```
## Output:
```
{
	“success”: true,
	“token”: “2a88364c-8108-4ea1-ae46-d90bfe060a7c”
	“message”: “Login successful, token returned.” 
}
```
---

# Login API Endpoints
## Enpoint: api/login

METHOD: <span style="color:orange">POST</span>

## Input:
```
{
  “username”: ”will123”,
  “password”: “password123!”
}
```
## Output:

 
Status Code: <span style="color:lightgreen">**Sucess, 200 OK**</span>
```
{
    “success”: true,
	“token”: “2a88364c-8108-4ea1-ae46-d90bfe060a7c”
	“message”: null
}
```

Status Code: <span style="color:red">**Error 401 Unauthorised**</span> (Incorrect Credentials):
```
{
    “success”: false,
    “token”: null,
    “message”: “Error 401 Unauthorised: Incorrect Credentials”
}
```
Status Code: <span style="color:red">**Error 403 Forbidden**</span>  (Login attempt limit exceeded) 
```
{
    “success”: false,
    “token”: null,
    “message”: “Error 403 Forbidden: Failed login attempts exceeded    please contact support”
}
```
Status Code: <span style="color:red">**Error 404 Not Found**</span> 
```
{
“success”: false,
“token”: null,
“message”: “Error 404 User Not Found, UserEntity.class”
}
```