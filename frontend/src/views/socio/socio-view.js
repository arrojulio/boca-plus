import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
import '@vaadin/vaadin-board/src/vaadin-board.js';

class SocioView extends PolymerElement {
  static get template() {
    return html`
<style include="shared-styles">
        :host {
          display: block;
        }
      </style>
<vaadin-board id="config-view">
 <h3>Pagina del Socio</h3>
</vaadin-board>
`;
  }

  static get is() {
    return 'socio-view';
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(SocioView.is, SocioView);
