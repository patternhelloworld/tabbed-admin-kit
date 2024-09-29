import React from 'react';
import PropTypes from 'prop-types';

const FooterText = (props) => (
	<React.Fragment>
		(C) { props.year } All Rights Reserved.
		Created by{' '}
		<a
			href="https://github.com/patternknife"
			target="_blank"
			rel="noopener noreferrer"
			className="sidebar__link"
		>
			Andrew Kang
		</a>
	</React.Fragment>
)
FooterText.propTypes = {
    year: PropTypes.node,
	name: PropTypes.node,
	desc: PropTypes.node,
};
FooterText.defaultProps = {
    year: "2024",
    name: "Autofocus",
    desc: "CRM & Logistics"
};

export { FooterText };
