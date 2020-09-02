import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';
import { MDBCol, MDBIcon } from "mdbreact";

export const EntitiesMenuMedecin = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
   
 
    <MenuItem icon="accessible-icon" to="/mesPatients">
    <MDBIcon fab icon="accessible-icon" />&nbsp; 
      <Translate contentKey="global.menu.entities.mesPatients" />
    </MenuItem>
  
    <MenuItem icon="asterisk" to="/groupe-de-patient">
      <Translate contentKey="global.menu.entities.groupeDePatient" />
    </MenuItem>
    
    <MenuItem icon="asterisk" to="/question">
      <Translate contentKey="global.menu.entities.question" />
    </MenuItem>

    <MenuItem icon="asterisk" to="/questionnaire">
      <Translate contentKey="global.menu.entities.questionnaire" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/patient-questionnaire">
      <Translate contentKey="global.menu.entities.patientQuestionnaire" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/notification">
      <Translate contentKey="global.menu.entities.notification" />
    </MenuItem>

    
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
