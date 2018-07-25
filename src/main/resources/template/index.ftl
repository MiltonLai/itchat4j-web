<#include "includes/overall_header.ftl">
<div class="container">
  <div class="py-5 text-center">

  </div>
  <div class="row">
    <div class="col-md-4 order-md-1 mb-4">
      <h4 class="d-flex justify-content-between align-items-center mb-3">
        <span class="text-muted">联系人</span>
        <span class="badge badge-secondary badge-pill">${contactList?size}</span>
      </h4>
      <ul class="list-group mb-3">
        <#list contactList as contact>
          <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
              <a href="chat.html?name=${contact.UserName}"><h6 class="my-0">${contact.NickName} (${contact.RemarkName})</h6></a>
              <small class="text-muted">${contact.Signature}</small>
            </div>
            <span class="text-muted">.</span>
          </li>
        </#list>
      </ul>
    </div>
    <div class="col-md-4 order-md-2 mb-4">
      <h4 class="d-flex justify-content-between align-items-center mb-3">
        <span class="text-muted">公众号</span>
        <span class="badge badge-secondary badge-pill">${publicUserList?size}</span>
      </h4>
      <ul class="list-group mb-3">
      <#list publicUserList as publicUser>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
          <div>
            <a href="chat.html?name=${publicUser.UserName}"><h6 class="my-0">${publicUser.NickName}</h6></a>
            <small class="text-muted">${publicUser.Signature}</small>
          </div>
          <span class="text-muted">.</span>
        </li>
      </#list>
      </ul>
    </div>
    <div class="col-md-4 order-md-3 mb-4">
      <h4 class="d-flex justify-content-between align-items-center mb-3">
        <span class="text-muted">群聊</span>
        <span class="badge badge-secondary badge-pill">${chatRooms?size}</span>
      </h4>
      <ul class="list-group mb-3">
      <#list chatRooms as room>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
          <div>
            <a href="chat.html?group=1&name=${room.name}"><h6 class="my-0">${room.nick} (${room.memberMap?size})</h6></a>
            <small class="text-muted"></small>
          </div>
          <span class="text-muted">.</span>
        </li>
      </#list>
      </ul>
    </div>
  </div>

</div>

<script>
  function flash() {
    $.ajax({type: "POST", url: "isAlive.html"}).done(function (data) {
      if (data == 'false') {
        location.reload();
      }
    });
  }
  setInterval(flash, 2000);
</script>
<#include "includes/overall_footer.ftl">
