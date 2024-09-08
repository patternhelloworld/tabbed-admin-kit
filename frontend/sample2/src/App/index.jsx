import React, { Fragment } from 'react';

import NormalizeStyles from './NormalizeStyles';
import BaseStyles from './BaseStyles';
import Toast from './Toast';
import Routes from './Routes';

// We're importing .css because @font-face in styled-components causes font files
// to be constantly re-requested from the server (which causes screen flicker)
// https://github.com/styled-components/styled-components/issues/1593
import './fontStyles.css';
import {ReactQueryProvider} from "../shared/providers/ReactQueryProvider";


// Fragment 설명 : https://ko.legacy.reactjs.org/docs/fragments.html
const App = () => (
  <Fragment>
    <NormalizeStyles />
    <BaseStyles />
    <Toast />
    <ReactQueryProvider>
        <Routes />
    </ReactQueryProvider>
  </Fragment>
);

export default App;
