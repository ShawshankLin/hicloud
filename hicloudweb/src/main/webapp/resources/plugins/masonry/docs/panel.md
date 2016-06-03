# Masonry 结合 Panel 使用
---

<div id="js-container">

</div>

<div id="load-more"><button class="am-btn am-btn-primary">载入更多</button></div>

<script type="text/x-handlebars-template" id="tpl-list">
  {{#each events}}
  <div class="msry-item">
    <section class="am-panel am-panel-default">
      <header class="am-panel-hd">
        <h3 class="am-panel-title">{{title}}</h3>
      </header>
      <div class="am-panel-bd">
        开始：{{begin_time}} <br/>
        结束： {{begin_time}}<br/>
        地点：{{address}} <br/>
        类型： {{category_name}} <br/>
        <a href="{{alt}}" target="_blank">查看详情 &rarrlp;</a>
      </div>
    </section>
  </div>
  {{/each}}
</script>

<script src="../masonry.pkgd.min.js"></script>
<script src="../bundle.js"></script>


