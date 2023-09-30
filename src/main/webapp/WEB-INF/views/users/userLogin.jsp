<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        form {
            max-width: 400px;
            width: 100%;
        }

        h3 {
            text-align: center;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group1 {
            margin-bottom: 15px;
        }

        .alert-danger {
            margin-top: 15px;
        }
    </style>

    <script>
        $(document).ready(function () {
            var loginError = "<%= request.getAttribute("loginError") %>";

            if (loginError && loginError.trim() !== "null") {
                $(".form-group1").prepend('<div class="alert alert-danger">' + loginError + '</div>');
                $("#id").focus();
            }
        });
    </script>
</head>

<body>
<form action="/users/login" method="post" class="p-3 border">

    <h3 class="mb-3">로그인</h3>

    <div class="form-group1">
        <label for="id" class="form-label">아이디:</label>
        <input type="text" class="form-control" id="id" name="id" required>
    </div>
    <div class="form-group">
        <label for="password" class="form-label">비밀번호:</label>
        <input type="password" class="form-control" id="password" name="password" required>
    </div>

    <button type="submit" class="btn btn-primary">로그인</button>
</form>
</body>

</html>
