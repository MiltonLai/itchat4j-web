<#include "includes/overall_header.ftl">
<div class="container">
  <div class="py-3">
    <ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="index.html">Home</a></li>
      <li class="breadcrumb-item active">${fromUser.NickName}<#if fromUser.MemberList?? && fromUser.MemberList?size &gt; 0>(${fromUser.MemberList?size})</#if></li>
    </ol>
  </div>
  <div class="row">
  <#if fromUser.MemberList?? && fromUser.MemberList?size &gt; 0>
    <div class="col-md-4 order-md-1 mb-4">
      <h4 class="d-flex justify-content-between align-items-center mb-3">
        <span class="text-muted">群聊成员</span>
        <span class="badge badge-secondary badge-pill">${fromUser.MemberList?size}</span>
      </h4>
      <ul class="list-group mb-3">
        <#list fromUser.MemberList as member>
          <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
              <h6 class="my-0">${member.NickName}</h6>
              <small class="text-muted">${member.Signature}</small>
            </div>
            <span class="text-muted">.</span>
          </li>
        </#list>
      </ul>
    </div>
    <div class="col-md-8 order-md-2 mb-4" id="msgList">

    </div>
  <#else>
    <div class="col-md-12 order-md-1 mb-4" id="msgList">

    </div>
  </#if>

  </div>
</div>

<script>

  function flash() {
    $.ajax({url: "isAlive.html", type: "post"}).done(function (data) {
      if (data === 'false') {
        location.reload();
      }
    });

    $.ajax({url: "conversation_msg.html", type: "post", data: {'name': '${fromUserName}'}})
        .done(function (data) {
          $('#msgList').empty();
          $.each(JSON.parse(data), function (n, msg) {
            var img = (msg.type == 'pic')? '<img src="file.html?path='+encodeURI(msg.filePath)+'" class="img-thumbnail">' : '';
            $('#msgList').append('<div class="alert alert-primary" role="alert"> ' + msg.fromNick + ": " + msg.content + img + '</div>');
          });
        });
  }

  function logout() {
    location.href = "logout.html";
  }

  setInterval(flash, 1000);
</script>
<#include "includes/overall_footer.ftl">
