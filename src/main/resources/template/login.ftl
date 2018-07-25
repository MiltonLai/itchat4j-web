<#include "includes/overall_header.ftl">
<div style="width: 100%;  max-width: 500px;  padding: 15px;  margin: auto;">
  <img src="QR.html">
</div>

<script>
  function flash() {
    $.ajax({type: "POST", url: "isAlive.html"}).done(function (data) {
        if (data == 'true') {
          location.reload();
        }
      });
  }
  setInterval(flash, 1000);
</script>
<#include "includes/overall_footer.ftl">
