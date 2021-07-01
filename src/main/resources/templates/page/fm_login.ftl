<!doctype html>
<html lang="en">
<#include "../common/fm_head.ftl">
<body>
    <div id="app" style="...">
        <form action="/fm/login" method="post">
            用户名:<input type="text" name="name" placeholder="用户名"/>
            <br/>
            密码:<input type="password" name="password" placeholder="密码"/>
            <br/>
            <input type="submit" value="登录">
        </form>

    </div>

</body>
</html>