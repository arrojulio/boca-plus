// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
    html {
    }

    [theme~="dark"] {
      --lumo-base-color: hsl(214, 54%, 18%);
      --lumo-primary-color: hsla(214, 82%, 23%, 0.48);
      --lumo-primary-text-color: hsl(214, 44%, 93%);
    }
  </style>
</custom-style>

<dom-module id="bakery-login-theme" theme-for="vaadin-login-overlay-wrapper">
  <template>
    <style>
      [part="brand"] {
        background-image: url(images/login-banner.jpg);
      }
    </style>
  </template>
</dom-module>
`;

document.head.appendChild($_documentContainer.content);
