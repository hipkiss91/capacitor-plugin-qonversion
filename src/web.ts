import { WebPlugin } from '@capacitor/core';

import type { QonversionPluginPlugin } from './definitions';

export class QonversionPluginWeb extends WebPlugin implements QonversionPluginPlugin {
    constructor() {
        super();
    }
}
