# -*- coding: utf-8 -*-
out=["I am good!", "I am evil!"]
bits = """
                                                            z��;/wB ���V���c���C�$��*(�CJe��̇���� ��_�d�]�N��{��r���D< _�Y�5
�����P`s�K�x`��ם:T����
�z����Rs�h�I)��~=�,"""
from hashlib import sha256
print out[0] if sha256(bits).hexdigest() == "34352b3c8ac538661c8591329147f6f57cc874affcb1f3909ca04ad242ea7a9a" else out[1]