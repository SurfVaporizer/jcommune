UPDATE CONTACT_TYPE SET VALIDATION_PATTERN='^[\\d]{21}$', MASK='21 numeric symbols', DISPLAY_PATTERN='<a href="http://plus.google.com/%s">%s</a>' WHERE lower(TYPE) = 'google+';