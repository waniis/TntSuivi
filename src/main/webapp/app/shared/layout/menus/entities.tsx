import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/groupe-de-patient">
      <Translate contentKey="global.menu.entities.groupeDePatient" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/region">
      <Translate contentKey="global.menu.entities.region" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/centre">
      <Translate contentKey="global.menu.entities.centre" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/specialty">
      <Translate contentKey="global.menu.entities.specialty" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/medicament">
      <Translate contentKey="global.menu.entities.medicament" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin-de-centre">
      <Translate contentKey="global.menu.entities.adminDeCentre" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/medecin">
      <Translate contentKey="global.menu.entities.medecin" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/patient">
      <Translate contentKey="global.menu.entities.patient" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/notification">
      <Translate contentKey="global.menu.entities.notification" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/questionnaire">
      <Translate contentKey="global.menu.entities.questionnaire" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/patient-questionnaire">
      <Translate contentKey="global.menu.entities.patientQuestionnaire" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/question">
      <Translate contentKey="global.menu.entities.question" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/question-answer">
      <Translate contentKey="global.menu.entities.questionAnswer" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/patient-reponse">
      <Translate contentKey="global.menu.entities.patientReponse" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/message">
      <Translate contentKey="global.menu.entities.message" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
