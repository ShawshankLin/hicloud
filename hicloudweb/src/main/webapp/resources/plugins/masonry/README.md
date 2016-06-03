# Masonry
---

Masonry 是一个瀑布流布局插件。本项目是 Masonry 与 Amaze UI 结合使用的示例。

完整信息请查看 [Masonry 官网](http://masonry.desandro.com)。

**结合 Amaze UI 使用演示：**

- [动态载入内容并使用 Panel 呈现](http://amazeui.github.io/masonry/docs/panel.html)

## 获取 Masonry

- **下载打包好的文件：**

  + [masonry.pkgd.min.js](masonry.pkgd.min.js)
  + [masonry.pkgd.js](masonry.pkgd.js)

- **使用 Bower：**

  ```
  bower install masonry
  ```

- **使用 NPM：**

  ```
  npm install masonry-layout
  ```

## 初始化

### 以 JavaScript 方式

``` js
var container = document.querySelector('#container');
var msnry = new Masonry( container, {
  // options...
  itemSelector: '.item',
  columnWidth: 200
});
```

### 以 HTML 方式

在元素上添加 `js-masonry` class，选项可以以 JSON 格式写在 `data-masonry-options` 属性上。

``` html
<div class="js-masonry" data-masonry-options='{ "itemSelector": ".item", "columnWidth": 200 }'>
  <div class="item"></div>
  <div class="item"></div>
  ...
</div>
```

## License

Masonry is released under the [MIT license](http://desandro.mit-license.org). Have at it.

Copyright :copyright: 2014 David DeSandro
