pkg_postinst_${PN}-core () {
    # several scritps assume /bin/python is available
    ln -sf ${bindir}/python $D${base_bindir}/python
}
    


