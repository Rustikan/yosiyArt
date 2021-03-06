<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Йоська</title>
</head>

<script src="/yosiay/static/js/jquery.min.js" type="text/javascript"></script>
<script src="/yosiay/static/js/xlsx.core.min.js" type="text/javascript"></script>
<script type="text/javascript">
    var prefix = '/yosiyArt_war';
    var fileName;

    var RestPost = function () {
        var f1 = $('#files');
        fileName = $('#files')[0].files[0];

        var reader = new FileReader();
        var name = fileName.name;
        reader.onload = function (fileName) {
            var data = fileName.target.result;

            var result;
            var workbook = XLSX.read(data, {type: 'binary'});

            var sheet_name_list = workbook.SheetNames;
            sheet_name_list.forEach(function (y) { /* iterate through sheets */
                //Convert the cell value to Json
                var roa = XLSX.utils.sheet_to_json(workbook.Sheets[y]);
                if (roa.length > 0) {
                    result = roa;
                }
            });

            var savefileName = "excel.xlsx";
            $.ajax({
                type: 'POST',
                url: prefix + '/post',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(result),
                async: true,
                success: function (responce, status, xhr) {
                    var blob = responce.substring(1, responce.length - 1);
                    console.log(1, blob);
                    if (window.navigator.msSaveOrOpenBlob) {
                        console.log(33333333);
                        window.navigator.msSaveBlob(blob, savefileName);
                    } else {
                        console.log(44444444444);
                        var downloadLink = window.document.createElement('a');
                        var contentTypeHeader = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"//xhr.getResponseHeader("content-type");
                        downloadLink.href = window.URL.createObjectURL(new Blob([blob], {type: contentTypeHeader}));
                        downloadLink.download = savefileName;
                        document.body.appendChild(downloadLink);
                        downloadLink.click();
                        document.body.removeChild(downloadLink);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);

                    var blob = jqXHR.responseText;
                    console.log(blob);

                    if (window.navigator.msSaveOrOpenBlob) {
                        console.log(33333333);
                        window.navigator.msSaveBlob(blob, savefileName);
                    } else {
                        console.log(44444444444);
                        var downloadLink = window.document.createElement('a');
                        var contentTypeHeader = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"//xhr.getResponseHeader("content-type");
                        downloadLink.href = window.URL.createObjectURL(new Blob([blob], {type: contentTypeHeader}));
                        downloadLink.download = savefileName;
                        document.body.appendChild(downloadLink);
                        downloadLink.click();
                        document.body.removeChild(downloadLink);
                    }
                    //alert(jqXHR.status + ' ' + jqXHR.responseText);
                }
            });
        };
        reader.readAsArrayBuffer(fileName);
    }

    function handleFile(e) {
        console.log(e);
        //Get the files from Upload control
        var files = e.target.files;
        var i, f;
        //Loop through files
        for (i = 0, f = files[i]; i != files.length; ++i) {
            var reader = new FileReader();
            var name = f.name;
            reader.onload = function (e) {
                var data = e.target.result;

                var result;
                var workbook = XLSX.read(data, {type: 'binary'});

                var sheet_name_list = workbook.SheetNames;
                sheet_name_list.forEach(function (y) { /* iterate through sheets */
                    //Convert the cell value to Json
                    var roa = XLSX.utils.sheet_to_json(workbook.Sheets[y]);
                    if (roa.length > 0) {
                        result = roa;
                    }
                });
                console.log(result);
                $('#restText').val(JSON.stringify(result));
            };
            reader.readAsArrayBuffer(f);
        }
    }

    var RestPost2 = function () {
        var JSONObject = [
            {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
            {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
            {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
            {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"}];
        $.ajax({
            type: 'POST',
            url: prefix + '/rs/orgs/01/invts/force',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(JSONObject),
            dataType: 'json',
            async: true,
            success: function (result) {
                alert('Кол-во: ' + result.length);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.status + ' ' + jqXHR.responseText);
            }
        });
    }

    //Change event to dropdownlist
    $(document).ready(function () {
        fileName = handleFile.target;
        $('#files').change(handleFile);
    });
</script>

<body>

<h3>Регистрация сотрудников на ГосУслугах</h3>

<form action="/yosiyArt_war/post" method="post">
    <input type="text" id="restText" name="restText" hidden="true"/>
    <input type="file" id="files" name="files"/>
    <br>
    <br>
    <label> Хост: </label>
    <%--<input type="text" id="host" name="host" value="http://localhost:8081/yosiyArt_war"/>--%>
    <input type="text" id="host" name="host" value="https://esia-portal1.test.gosuslugi.ru"/>
    <br>
    <br>
    <label>ИД организации</label>
    <input type="text" id="orgId" name="orgId" value="1000000001"/>
    <br>
    <br>
    <label>ИД ЦО</label>
    <input type="text" id="rcOid" name="rcOid" value="01"/>
    <br>
    <br>
    <div class="row">
        <div class="col-md-2 text-center">
            <input type="submit" class="btn btn-success" id="btnPostEmpl" name="btnPostEmpl"
                   value="Отправить запрос на регистрацию сотрудников"/>
        </div>
        <br>
        <div class="col-md-2 text-center">
            <input type="submit" class="btn btn-success" id="btnPostToOrg" name="btnPostToOrg"
                   value="Отправить запрос на прикрепление сотрудников"/>
        </div>
    </div>
</form>

<form action="/yosiay/auth/getCode" method="get">
    <label> Хост: </label>
   <%-- <input type="text" id="hostGet" name="hostGet" value="http://localhost:8081/yosiyArt_war"/>--%>

    <input type="text" id="hostGet" name="hostGet" value="https://esia-portal1.test.gosuslugi.ru"/>

    <br>
    <br>
    <label>AccessType:</label>
    <input type="text" id="accessType" name="accessType" value="offline"/>
   <br>
    <br>
    <label>Scope:</label>
    <input type="text" id="scope" name="scope" value="snils"/>
   <br>
    <br>
    <label> Мнемоника системы: </label>
    <input type="text" id="client_id" name="client_id" value="771601"/>
    <br>
    <br>
    <label> Обратный УРЛ: </label>
    <input type="text" id="backUrl" name="backUrl" value=""/>
    <br>
    <br>
    <label> Подпись: </label>
    <input type="text" id="sign" name="sign" />
    <br>
    <br>
    <div class="col-md-2 text-center">
        <input type="submit" class="btn btn-success" id="btnGetEmpl" name="btnGettEmpl"
               value="Отправить запрос"/>
    </div>
</form>

<form action="/yosiay/auth/getCode2" method="get">
    <br>
    <label>Authorization:</label>
    <input type="text" id="bearer2" name="bearer2" value="Bearer 76d0d4ee-4edb-4366-b5ce-f168b1ee1c7f"/>
    <br>
    <br>
    <label> Хост сервиса подписаний: </label>
    <input type="text" id="hostSigner2" name="hostSigner2" value="http://10.4.116.20/bsb/crypto/sign/pkcs7"/>
    <br>
    <br>
    <div class="col-md-2 text-center">
        <input type="submit" class="btn btn-success" id="btnGetEmpl2" name="btnGettEmpl2"
               value="Получить подпись"/>
    </div>

</form>
</body>
</html>