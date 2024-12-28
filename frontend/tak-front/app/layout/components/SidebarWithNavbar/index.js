import { SidebarWithNavbar } from './SidebarWithNavbar';
import {
    DefaultSidebar
} from '../DefaultSidebar';
import {
    SidebarWithNavbarNavbarCustom
} from './SidebarWithNavbarNavbarCustom';

SidebarWithNavbar.Navbar = SidebarWithNavbarNavbarCustom;
SidebarWithNavbar.Sidebar = DefaultSidebar;

export default SidebarWithNavbar;
