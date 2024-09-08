import React, {Suspense} from 'react';
import { RecoilRoot } from "recoil";
import { BrowserRouter as Router } from 'react-router-dom';
import history from './browserHistory';

import AppLayout from '../layout/default';
import { RoutedContent } from './routes';
import {ReactQueryProvider} from "../shared/providers/ReactQueryProvider";

import { TabsProvider } from '../shared/providers/TabsContext';

import RecoilizeDebugger from 'recoilize';

const basePath = process.env.BASE_PATH || '/';

const rootForRecoilizeDebugger = document.getElementById('root');
//
const AppClient = () => {
    return (
        <RecoilRoot>
            <RecoilizeDebugger root={rootForRecoilizeDebugger} />
            <Router basename={ basePath } history={history}>
                <Suspense fallback={<div>Loading...</div>}>
                    <TabsProvider >
                        <AppLayout>
                            <ReactQueryProvider>
                                <RoutedContent/>
                            </ReactQueryProvider>
                        </AppLayout>
                    </TabsProvider>
                </Suspense>
            </Router>
        </RecoilRoot>
    );
}

export default AppClient;