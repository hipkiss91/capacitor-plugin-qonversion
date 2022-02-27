import { registerPlugin } from '@capacitor/core';

import type { QonversionPluginPlugin } from './definitions';

const QonversionPlugin = registerPlugin<QonversionPluginPlugin>('QonversionPlugin', {
  // web: () => import('./web').then(m => new m.QonversionPluginWeb()),
});

export * from './definitions';
export { QonversionPlugin };
