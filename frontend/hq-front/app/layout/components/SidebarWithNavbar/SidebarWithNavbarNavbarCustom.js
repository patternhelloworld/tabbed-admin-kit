import React, {useState} from 'react';
import { Link } from 'react-router-dom';

import {
    Navbar,
    Nav,
    NavItem,
    NavLink,
    NavbarToggler,
    UncontrolledCollapse,
    SidebarTrigger,
    ThemeConsumer
} from '../../../components';

import { NavbarActivityFeed } from '../NavbarActivityFeed';
import { NavbarMessages } from '../NavbarMessages';
import { NavbarUser } from '../NavbarUser';
import { LogoThemed } from '../../../components/LogoThemed/LogoThemed';
import '../../../styles/components/tabs.scss';
import { useTabs } from '../../../shared/providers/TabsContext';


export const SidebarWithNavbarNavbarCustom = () => {
    const { tabs, activeTab, setActiveTab, removeTab } = useTabs();

    return (
        <React.Fragment>
            <ThemeConsumer>
                {
                    ({color}) => (
                        <React.Fragment>
                            { /*    First Navbar    */}
                            <Navbar
                                light
                                expand
                                fluid
                                className="bg-white pb-0 pb-lg-2"
                            >
                                <Nav navbar>
                                    <NavItem>
                                        <SidebarTrigger/>
                                    </NavItem>
                                    <NavItem className="navbar-brand d-lg-none">
                                        <Link to="/">
                                            <LogoThemed/>
                                        </Link>
                                    </NavItem>
                                </Nav>

                                <h1
                                    className="h5 mb-0 mr-auto ml-2 d-none d-lg-block"
                                >

                                </h1>

                                <Nav navbar className="ml-auto">
                                {/*    <NavbarActivityFeed/>
                                    <NavbarMessages className="ml-2"/>*/}
                                    <NavbarUser className="ml-2"/>
                                </Nav>
                            </Navbar>
                            { /*    Second Navbar    */}
                            <Navbar
                                shadow
                                expand="lg"
                                light
                                color={color}
                                fluid
                                className="pt-0 pt-lg-2"
                            >
                                <h1 className="h5 mb-0 py-2 mr-auto d-lg-none">

                                </h1>
                                <UncontrolledCollapse navbar toggler="#navbar-navigation-toggler">
                                    <Nav accent navbar>
                                        {tabs.map((tab, index) => (
                                            <NavItem key={index} className={`nav-item-wrapper ${index === activeTab ? 'active' : ''}`}>
                                               <NavLink
                                                    active={index === activeTab}
                                                    onClick={() => setActiveTab(index)}
                                                    tag={Link}
                                                    to={tab.to}
                                                >
                                                    {tab.title}
                                                </NavLink>
                                                <i onClick={(e) => { e.stopPropagation(); removeTab(index); }}
                                                   className="fa fa-fw fa-close nav-item-icon" aria-hidden="true"     style={{ cursor: 'pointer' }}></i>
                                            </NavItem>
                                        ))}
                                        {/*<NavItem>
                                            <button className="add-tab-button" onClick={addTab}>+</button>
                                        </NavItem>*/}
                                    </Nav>
                                </UncontrolledCollapse>
                                <Nav navbar pills className="ml-auto">
                                    <NavItem>
                                        <NavLink tag={NavbarToggler} id="navbar-navigation-toggler" className="b-0">
                                            <i className="fa fa-ellipsis-h fa-fw"></i>
                                        </NavLink>
                                    </NavItem>
                                </Nav>
                            </Navbar>
                        </React.Fragment>
                    )
                }
            </ThemeConsumer>
        </React.Fragment>
    );
}