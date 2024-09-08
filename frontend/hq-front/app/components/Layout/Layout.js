import React, { useState, useEffect, useRef, useContext } from 'react';
import ReactDOM from 'react-dom';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { Helmet } from 'react-helmet';
import {useHistory, useLocation, withRouter} from 'react-router-dom';
import _ from 'lodash';

import { LayoutContent } from './LayoutContent';
import { LayoutNavbar } from './LayoutNavbar';
import { LayoutSidebar } from './LayoutSidebar';
import { PageConfigContext } from './PageConfigContext';
import { ThemeClass } from './../Theme';

import config from './../../../config';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { globalInfoAccessTokenUserInfoSelector, globalInfoSidebarCollapsedSelector } from '../../shared/recoil/globalInfoState';

const findChildByType = (children, targetType) => {
    let result;

    React.Children.forEach(children, (child) => {
        if (child.type.layoutPartName === targetType.layoutPartName) {
            result = child;
        }
    });

    return result;
};

const findChildrenByType = (children, targetType) => {
    return _.filter(React.Children.toArray(children), (child) =>
        child.type.layoutPartName === targetType.layoutPartName
    );
};

const responsiveBreakpoints = {
    'xs': { max: 575.8 },
    'sm': { min: 576, max: 767.8 },
    'md': { min: 768, max: 991.8 },
    'lg': { min: 992, max: 1199.8 },
    'xl': { min: 1200 }
};

const Layout = ({ children, sidebarSlim, favIcons }) => {

    const globalSidebarCollapsed = useRecoilValue(globalInfoSidebarCollapsedSelector());
    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());


    const [sidebarHidden, setSidebarHidden] = useState(false);
    const [navbarHidden, setNavbarHidden] = useState(false);
    const [footerHidden, setFooterHidden] = useState(false);
    const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
    const [screenSize, setScreenSize] = useState('');
    const [animationsDisabled, setAnimationsDisabled] = useState(true);
    const [pageTitle, setPageTitle] = useState(null);
    const [pageDescription, setPageDescription] = useState(config.siteDescription);
    const [pageKeywords, setPageKeywords] = useState(config.siteKeywords);
    const lastLgSidebarCollapsed = useRef(false);
    const containerRef = useRef(null);
    const bodyElement = useRef(document.body);
    const documentElement = useRef(document.documentElement);
    const history = useHistory();
    const location = useLocation();


    const updateNavbarsPositions = () => {
        const containerElement = containerRef.current;
        if (containerElement) {
            const navbarElements = containerElement.querySelectorAll(":scope .layout__navbar");

            let totalNavbarsHeight = 0;
            navbarElements.forEach((navbarElement) => {
                const navbarBox = navbarElement.getBoundingClientRect();
                navbarElement.style.top = `${totalNavbarsHeight}px`;
                totalNavbarsHeight += navbarBox.height;
            });
        }
    };

    useEffect(() => {
        const layoutAdjuster = () => {
            let currentScreenSize;

            _.forOwn(responsiveBreakpoints, (value, key) => {
                const queryParts = [
                    `${_.isUndefined(value.min) ? '' : `(min-width: ${value.min}px)`}`,
                    `${_.isUndefined(value.max) ? '' : `(max-width: ${value.max}px)`}`
                ];
                const query = _.compact(queryParts).join(' and ');

                if (window.matchMedia(query).matches) {
                    currentScreenSize = key;
                }
            });

            if (screenSize !== currentScreenSize) {
                setScreenSize(currentScreenSize);
                updateLayoutOnScreenSize(currentScreenSize);
            }
        };


        const handleResize = () => {
            setTimeout(layoutAdjuster, 0);
        };

        window.addEventListener('resize', handleResize);
        layoutAdjuster();

        window.requestAnimationFrame(() => {
            setAnimationsDisabled(false);
        });

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, [screenSize]);

    useEffect(()=>{

        setSidebarCollapsed(globalSidebarCollapsed.value)

    },[globalSidebarCollapsed])

    useEffect(() => {
        if (['xs', 'sm', 'md'].includes(screenSize)) {
            if (sidebarCollapsed !== lastLgSidebarCollapsed.current) {
                const styleUpdate = sidebarCollapsed ? {
                    overflowY: 'auto',
                    touchAction: 'auto'
                } : {
                    overflowY: 'hidden',
                    touchAction: 'none'
                };
                Object.assign(bodyElement.current.style, styleUpdate);
                Object.assign(documentElement.current.style, styleUpdate);
            }
        }

        if (location.pathname !== history.location.pathname) {
            if (bodyElement.current && documentElement.current) {
                documentElement.current.scrollTop = bodyElement.current.scrollTop = 0;
            }

            if (!sidebarCollapsed && ['xs', 'sm', 'md'].includes(screenSize)) {
                setTimeout(() => {
                    setSidebarCollapsed(true);
                }, 100);
            }
        }

        updateNavbarsPositions();
    }, [screenSize, sidebarCollapsed, location.pathname, history.location.pathname]);

    const updateLayoutOnScreenSize = (screenSize) => {
        if (['md', 'sm', 'xs'].includes(screenSize)) {
            lastLgSidebarCollapsed.current = sidebarCollapsed;
            setSidebarCollapsed(true);
        } else {
            setSidebarCollapsed(lastLgSidebarCollapsed.current);
        }
    };

    const toggleSidebar = () => {
        setSidebarCollapsed(!sidebarCollapsed);
    };

    const setElementsVisibility = (elements) => {
        const { sidebarHidden, navbarHidden, footerHidden } = elements;
        setSidebarHidden(sidebarHidden);
        setNavbarHidden(navbarHidden);
        setFooterHidden(footerHidden);
    };

    const sidebar = findChildByType(children, LayoutSidebar);
    const navbars = findChildrenByType(children, LayoutNavbar);
    const content = findChildByType(children, LayoutContent);
    const otherChildren = _.differenceBy(
        React.Children.toArray(children),
        [
            sidebar,
            ...navbars,
            content
        ],
        'type'
    );

    const layoutClass = classNames('layout', 'layout--animations-enabled');

    return (
        <PageConfigContext.Provider
            value={{
                sidebarHidden,
                navbarHidden,
                footerHidden,
                sidebarCollapsed,
                screenSize,
                animationsDisabled,
                pageTitle,
                pageDescription,
                pageKeywords,
                sidebarSlim: !!sidebarSlim && (screenSize === 'lg' || screenSize === 'xl'),
                toggleSidebar,
                setElementsVisibility,
                changeMeta: (metaData) => {
                    setPageTitle(metaData.pageTitle);
                    setPageDescription(metaData.pageDescription);
                    setPageKeywords(metaData.pageKeywords);
                }
            }}
        >
            <Helmet>
                <meta charSet="utf-8" />
                <title>{config.siteTitle + (pageTitle ? ` - ${pageTitle}` : '')}</title>
                <link rel="canonical" href={config.siteCannonicalUrl} />
                <meta name="description" content={pageDescription} />
                {
                    _.map(favIcons, (favIcon, index) => (
                        <link { ...favIcon } key={ index } />
                    ))
                }
            </Helmet>
            <ThemeClass>
                {(themeClass) => (
                    <div className={classNames(layoutClass, themeClass)} ref={containerRef}>
                        {
                            !sidebarHidden && sidebar && React.cloneElement(sidebar, {
                                sidebarSlim: !!sidebarSlim && sidebarCollapsed && (screenSize === 'lg' || screenSize === 'xl'),
                                sidebarCollapsed: !sidebarSlim && sidebarCollapsed
                            })
                        }

                        <div className="layout__wrap">
                            {!navbarHidden && navbars}
                            {content}
                        </div>

                        {otherChildren}
                    </div>
                )}
            </ThemeClass>
        </PageConfigContext.Provider>
    );
};

Layout.propTypes = {
    children: PropTypes.node,
    sidebarSlim: PropTypes.bool,
    favIcons: PropTypes.array,
};

const RoutedLayout = withRouter(Layout);

export { RoutedLayout as Layout };
